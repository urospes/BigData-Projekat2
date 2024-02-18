import sys
from pyspark.sql import SparkSession
from pyspark.sql.types import StringType #StructType, StructField, IntegerType, FloatType, BooleanType
import pyspark.sql.functions as F

def make_zone_udf(center_point):
    def get_zone(lat, long, center_point=center_point):
        if lat > center_point[0]:
            if long > center_point[1]:
                return "NE"
            else:
                return "NW"
        else:
            if long > center_point[1]:
                return "SE"
            else:
                return "SW"
    
    return F.udf(get_zone, StringType())

def get_value_schema():
    # return StructType([StructField("timestep", IntegerType(), False),
    #                    StructField("vehicle_CO", FloatType(), False),
    #                    StructField("vehicle_CO2", FloatType(), False),
    #                    StructField("vehicle_HC", FloatType(), False),
    #                    StructField("vehicle_NOx", FloatType(), False),
    #                    StructField("vehicle_PMx", FloatType(), False),
    #                    StructField("vehicle_fuel", FloatType(), False),
    #                    StructField("vehicle_id", StringType(), False),
    #                    StructField("vehicle_lane", StringType(), False),
    #                    StructField("vehicle_noise", FloatType(), False),
    #                    StructField("vehicle_type", StringType(), False),
    #                    StructField("vehicle_waiting", BooleanType(), False),
    #                    StructField("vehicle_x", FloatType(), False),
    #                    StructField("vehicle_y", FloatType(), False)])
    return "timestep float, vehicle_CO float, vehicle_CO2 float, vehicle_HC float, vehicle_NOx float, vehicle_PMx float, vehicle_fuel float," \
            "vehicle_id string, vehicle_lane string, vehicle_noise float, vehicle_type string, vehicle_waiting float, vehicle_x float, vehicle_y float"

def parse_lane_name(lane_name):
    # Parameters
    # -------------
    # lane_name: String -> lane_name in form "lane_id"_"lane_num" or "lane_id"#"lane_num"

    # Returns
    # -------------
    # lane_id: String
    lane_name = lane_name.replace(":", "").replace("-", "")
    if "#" in lane_name:
        return lane_name.split("#")[0]
    else:
        return lane_name.split("_")[0]


def main(args):

    kafka_broker = "kafka:9092"
    source = "traffic_data"
    sink = "results"
    tumbling = len(args) == 1
    window_size = args[0]
    window_slide = args[1] if not tumbling else None
    center_point = (23.726766, 37.984473)

    spark = SparkSession.builder.appName("SmartCityMobility").getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    data = spark.readStream \
                .format("kafka") \
                .option("kafka.bootstrap.servers", kafka_broker) \
                .option("subscribe", source) \
                .load()

    data = data.selectExpr("timestamp as event_time", "CAST(key AS STRING)", "CAST(value AS STRING)", "CAST(offset as int)")

    schema = get_value_schema()
    data_deserialized = data.withColumn("value", F.from_csv(data["value"], schema=schema, options={"delimiter": ";"}))

    flat_df = data_deserialized.selectExpr("event_time", "offset", "key as vehicle_id",
                                            "CAST(value.vehicle_CO as float) as vehicle_CO", "CAST(value.vehicle_CO2 as float) as vehicle_CO2",
                                            "CAST(value.vehicle_HC as float) as vehicle_HC", "CAST(value.vehicle_NOx as float) as vehicle_NOx",
                                            "CAST(value.vehicle_PMx as float) as vehicle_PMx", "CAST(value.vehicle_fuel as float) as vehicle_fuel",
                                            "CAST(value.vehicle_lane as string) as vehicle_lane", "CAST(value.vehicle_noise as float) as vehicle_noise",
                                            "CAST(value.vehicle_type as string) as vehicle_type", "CAST(value.vehicle_waiting as float) as vehicle_waiting",
                                            "CAST(value.vehicle_x as float) as vehicle_x", "CAST(value.vehicle_y as float) as vehicle_y") \
    
    zone_udf = make_zone_udf(center_point)
    final_df = flat_df.withColumn("zone", zone_udf(flat_df["vehicle_x"], flat_df["vehicle_y"]))



    # streaming queries -------------------------------------------------------------------------------------------
    # zadatak 1
    window = F.window(F.col("event_time"), window_size) if tumbling else \
             F.window(F.col("event_time"), window_size, window_slide)
    
    grouped_zone = final_df.withWatermark("event_time", "20 seconds") \
                           .groupby(window, "zone")
    
    agg_CO = grouped_zone.agg(F.max("vehicle_CO"), F.avg("vehicle_CO"))
    agg_CO2 = grouped_zone.agg(F.max("vehicle_CO2"), F.avg("vehicle_CO2"))
    agg_HC = grouped_zone.agg(F.max("vehicle_HC"), F.avg("vehicle_HC"))
    agg_PMx = grouped_zone.agg(F.max("vehicle_PMx"), F.avg("vehicle_PMx"))
    agg_NOx = grouped_zone.agg(F.max("vehicle_NOx"), F.avg("vehicle_NOx"))
    agg_fuel = grouped_zone.agg(F.max("vehicle_fuel"), F.avg("vehicle_fuel"))


    agg_cols = ("CO", "CO2", "HC", "PMx", "NOx", "fuel")
    for i, agg in enumerate((agg_CO, agg_CO2, agg_HC, agg_PMx, agg_NOx, agg_fuel)):
        max_col = "max(vehicle_" + agg_cols[i]
        avg_col = "avg(vehicle_" + agg_cols[i]
        checkpointLoc = "/tmp/checpoint" + agg_cols[i]
        agg_type = "agg_emission_" + agg_cols[i]

        agg.withColumn("agg_type", F.lit(agg_type)) \
            .withColumn("value", F.encode(F.to_json(F.struct(F.col("*"))), "iso-8859-1")) \
            .drop("window", "zone", max_col, avg_col, "agg_type") \
            .writeStream \
            .queryName(f"{agg_cols[i]}_agg") \
            .format("kafka") \
            .option("kafka.bootstrap.servers", kafka_broker) \
            .option("topic", sink) \
            .option("checkpointLocation", checkpointLoc) \
            .outputMode("append") \
            .start()
    
    #zadatak 2
    lane_parser_udf = F.udf(parse_lane_name, StringType())

    grouped_lanes = final_df.withWatermark("event_time", "20 seconds") \
                            .withColumn("vehicle_lane", lane_parser_udf("vehicle_lane")) \
                            .groupby(window, "vehicle_lane") \
                            .count()
    
    grouped_lanes.withColumn("agg_type", F.lit("per_lane_agg")) \
                 .withColumn("value", F.encode(F.to_json(F.struct(F.col("*"))), "iso-8859-1")) \
                 .drop("window", "parse_lane_name(vehicle_lane)", "count", "per_lane_agg") \
                 .writeStream \
                 .queryName("lane_agg") \
                 .format("kafka") \
                 .option("kafka.bootstrap.servers", kafka_broker) \
                 .option("topic", sink) \
                 .option("checkpointLocation", "/tmp/checkpoint/lanes") \
                 .outputMode("append") \
                 .start()

    spark.streams.awaitAnyTermination()
    spark.stop()



if __name__ == "__main__":
    for arg in sys.argv:
        print(arg)
    if len(sys.argv) != 2 and len(sys.argv) != 3:
        print("Invalid input parameters...")
        exit(1)
    
    args = tuple(map(lambda arg: arg.replace("_", " "), sys.argv[1:]))
    main(args)

package org.smartcity;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.smartcity.dtos.FullLaneOutputRecord;
import org.smartcity.dtos.FullPollutionOutputRecord;
import org.smartcity.dtos.MessageDTO;
import java.time.Duration;

public class FlinkStreamingApp {

    private static Time parseTimeString(String timeDurationString, int value) {
        switch (timeDurationString) {
            case "seconds":
            case "second":
                return Time.seconds(value);
            case "minutes":
            case "minute":
                return Time.minutes(value);
            case "hours":
            case "hour":
                return Time.hours(value);
            default:
                return null;
        }
    }
    private static Tuple2<Time, Time> getWindowDurationFromArgs(String[] args) throws Exception {
        String[] parsedArgs = args[0].split("_");

        Time windowDuration = parseTimeString(parsedArgs[1], Integer.parseInt(parsedArgs[0]));
        if (windowDuration == null) {
            System.out.println("Invalid WindowDuration arguments... Accepting only ('seconds', 'minutes', 'hours')");
            throw new Exception("Invalid WindowDuration arguments... Accepting only ('seconds', 'minutes', 'hours')");
        }

        Time windowSlide = null;
        if (args.length == 2) {
            parsedArgs = args[1].split("_");
            windowSlide = parseTimeString(parsedArgs[1], Integer.parseInt(parsedArgs[0]));
            if (windowSlide == null) {
                System.out.println("Invalid WindowSlide arguments... Accepting only ('seconds', 'minutes', 'hours')");
                throw new Exception("Invalid WindowSlide arguments... Accepting only ('seconds', 'minutes', 'hours')");
            }
        }

        return new Tuple2<>(windowDuration, windowSlide);
    }
    private static WindowAssigner<Object, TimeWindow> createTimeWindow(Tuple2<Time, Time> windowParams) {
        if (windowParams.f1 == null) {
            return TumblingEventTimeWindows.of(windowParams.f0);
        }
        else {
            return SlidingEventTimeWindows.of(windowParams.f0, windowParams.f1);
        }
    }



    public static void main(String[] args) throws Exception {
        if (args.length != 1 && args.length != 2) {
            throw new Exception("Invalid input arguments... Application requires 1 or 2 TimeWindow arguments.");
        }

        Tuple2<Time, Time> windowParams = getWindowDurationFromArgs(args);
        String kafkaBroker = "kafka:9092";
        String sourceTopic = "traffic_data";
        String pollutionSinkTopic = "results_pollution";
        String lanesSinkTopic = "results_lanes";

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<MessageDTO> source = KafkaSource.<MessageDTO>builder()
                .setBootstrapServers(kafkaBroker)
                .setTopics(sourceTopic)
                .setGroupId("flink_stream_grp")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new MessageDeserializer())
                .build();

        WindowAssigner<Object, TimeWindow> window = createTimeWindow(windowParams);

        DataStream<MessageDTO> dataStream = env.fromSource(source, WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(10)), "SUMODataSource")
                .filter(record -> record.get_vehicleID() != null);

        // ---- Zadatak 1 ------------------------------------

        KafkaSink<FullPollutionOutputRecord> pollutionSink = KafkaSink.<FullPollutionOutputRecord>builder()
                .setBootstrapServers(kafkaBroker)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(pollutionSinkTopic)
                        .setValueSerializationSchema(new PollutionOutputSerializer())
                        .build())
                .build();

        DataStream<FullPollutionOutputRecord> pollutionStream = dataStream.process(new ZoneAttachmentTransformer())
                .keyBy(record -> record.get_zone())
                .window(window)
                .aggregate(new PollutionAggregation.PollutionAggregator(), new PollutionAggregation.ContextExtractorFunction());

        pollutionStream.sinkTo(pollutionSink);

        // ---- Zadatak 2 ------------------------------------

        KafkaSink<FullLaneOutputRecord> perLaneSink = KafkaSink.<FullLaneOutputRecord>builder()
                .setBootstrapServers(kafkaBroker)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(lanesSinkTopic)
                        .setValueSerializationSchema(new LaneOutputSerializer())
                        .build())
                .build();

        DataStream<FullLaneOutputRecord> laneStream =  dataStream.keyBy(record -> record.get_vehicleLane())
                .window(window)
                .aggregate(new LaneAggregation.CountPerLane(), new LaneAggregation.ContextExtractorFunction())
                .windowAll(window)
                .maxBy("_vehicleCount");

        laneStream.sinkTo(perLaneSink);

        env.execute("Athens StreamingData");
    }
}
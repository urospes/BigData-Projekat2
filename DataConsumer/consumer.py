import sys
import json
from confluent_kafka import Consumer
from cassandra.cluster import Cluster
import uuid

def handle_message(msg, session, queries):
    print("Message received.")
    decoded_msg = msg.value().decode("iso-8859-1")
    parsed_msg = json.loads(decoded_msg)
    if parsed_msg["agg_type"] == "pollution_agg":
        values = (str(uuid.uuid4()), parsed_msg["agg_type"], parsed_msg["window_start"], parsed_msg["window_end"], parsed_msg["zone"], parsed_msg["pollution"]["sum(vehicle_CO)"], \
                  parsed_msg["pollution"]["sum(vehicle_CO2)"], parsed_msg["pollution"]["sum(vehicle_HC)"], parsed_msg["pollution"]["sum(vehicle_PMx)"], \
                  parsed_msg["pollution"]["sum(vehicle_NOx)"], parsed_msg["pollution"]["sum(vehicle_fuel)"])
        
        session.execute(queries["agg_pollution"], values)
    else:
        values = (str(uuid.uuid4()), parsed_msg["agg_type"], parsed_msg["window_start"], parsed_msg["window_end"], parsed_msg["vehicle_lane"], parsed_msg["vehicle_count"])
        session.execute(queries["agg_lane"], values)
    return True



def prepare_queries(session, table):
    queries = dict()
    query = f'INSERT INTO {table} (id, agg_type, time_from, time_to, zone, sum_CO, sum_CO2, sum_HC, sum_PMx, sum_NOx, sum_fuel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'
    queries["agg_pollution"] = session.prepare(query)

    query = f'INSERT INTO {table} (id, agg_type, time_from, time_to, lane, vehicle_count) VALUES (?, ?, ?, ?, ?, ?)'
    queries["agg_lane"] = session.prepare(query)

    return queries

    


def main(args):
    running = True
    topics = ["results_pollution", "results_lanes"]
    conf = {
        "bootstrap.servers": "kafka:9092",
        "auto.offset.reset": "smallest",
        "group.id": "tfd_consumers"
    }
    consumer = Consumer(conf)
    cassandra_cluster = Cluster(["cassandra-1"], port=9042)

    try:
        consumer.subscribe(topics)
        print(f"Kafka broker: {conf['bootstrap.servers']}")
        print(f"Successfully subscribed to topics: {topics[0], topics[1]}")
        session = cassandra_cluster.connect(keyspace="mobility")
        print(f"Successfully connected to cassandra cluster. Using keyspace {session.keyspace}")
        queries = prepare_queries(session, "traffic_data")

        print("Consumer loop started...")
        while running:
            msg = consumer.poll(timeout=3.0)
            if msg is None: continue
            if msg.error():
                print(msg.error())
            else:
                running = handle_message(msg, session, queries)
    finally:
        print("Closing kafka consumer...")
        consumer.close()
        print("Closing cassandra session...")
        cassandra_cluster.shutdown()

if __name__ == "__main__":
    main(sys.argv)

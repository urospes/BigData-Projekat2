import sys
import json
from confluent_kafka import Consumer
from cassandra.cluster import Cluster
import uuid

def handle_message(msg, session, queries):
    print("Message received.")
    decoded_msg = msg.value().decode("iso-8859-1")
    parsed_msg = json.loads(decoded_msg)
    if parsed_msg["agg_type"] == "per_lane_agg":
        values = (str(uuid.uuid4()), parsed_msg["window"]["start"], parsed_msg["window"]["end"], "agg_per_lane", parsed_msg["vehicle_lane"], parsed_msg["count"])
        session.execute(queries["agg_lane"], values)
        return True
    if "agg_emission" in parsed_msg["agg_type"]:
        emission_param = parsed_msg["agg_type"].split("_")[2]
        values = (str(uuid.uuid4()), parsed_msg["window"]["start"], parsed_msg["window"]["end"], "emission_agg", parsed_msg["zone"], parsed_msg[f"max(vehicle_{emission_param})"], parsed_msg[f"avg(vehicle_{emission_param})"])
        session.execute(queries[f"agg_{emission_param}"], values)
        return True
    return False



def prepare_queries(session, table):
    queries = dict()
    for emission_param in ("CO", "CO2", "HC", "PMx", "NOx", "fuel"):
        query_name = f"agg_{emission_param}"
        query = f'INSERT INTO {table} (id, time_from, time_to, agg_type, zone, max_{emission_param}, avg_{emission_param}) VALUES (?, ?, ?, ?, ?, ?, ?)'
        queries[query_name] = session.prepare(query)
    
    query = f'INSERT INTO {table} (id, time_from, time_to, agg_type, lane, num_vehicles) VALUES (?, ?, ?, ?, ?, ?)'
    queries["agg_lane"] = session.prepare(query)

    return queries

    


def main(args):
    running = True
    topic = "results"
    conf = {
        "bootstrap.servers": "kafka:9092",
        "auto.offset.reset": "smallest",
        "group.id": "tfd_consumers"
    }
    consumer = Consumer(conf)
    cassandra_cluster = Cluster(["cassandra-1"], port=9042)

    try:
        consumer.subscribe([topic])
        print(f"Kafka broker: {conf['bootstrap.servers']}")
        print(f"Successfully subscribed to topic: {topic}")
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

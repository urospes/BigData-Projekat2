-- Create a keyspace
CREATE KEYSPACE IF NOT EXISTS mobility WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '2' };

-- Create a table
CREATE TABLE IF NOT EXISTS mobility.traffic_data (id text PRIMARY KEY, time_from text, time_to text, agg_type text, lane text, vehicle_count int, zone text, sum_CO float, sum_CO2 float, sum_HC float, sum_PMx float, sum_NOx float, sum_fuel float);

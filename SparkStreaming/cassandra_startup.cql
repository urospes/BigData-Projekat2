-- Create a keyspace
CREATE KEYSPACE IF NOT EXISTS mobility WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '2' };

-- Create a table
CREATE TABLE IF NOT EXISTS mobility.traffic_data (id text PRIMARY KEY, time_from text, time_to text, agg_type text, lane text, num_vehicles int, zone text, max_CO float, avg_CO float, max_CO2 float, avg_CO2 float, max_HC float, avg_HC float, max_PMx float, avg_PMx float, max_NOx float, avg_NOx float, max_fuel float, avg_fuel float);

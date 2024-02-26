package org.smartcity;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MsgProducer {
    public static KafkaProducer<String, DataEntryDTO> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DTOSerializer.class);
        return new KafkaProducer<>(props);
    }

    public static DataEntryDTO toDTOObject(String lineCsv){
        List<String> attrs = Arrays.asList(lineCsv.split(";"));

        int time = (int)Float.parseFloat(attrs.get(0));
        float co = Float.parseFloat(attrs.get(1));
        float co2 = Float.parseFloat(attrs.get(2));
        float hc = Float.parseFloat(attrs.get(3));
        float nox = Float.parseFloat(attrs.get(4));
        float pmx = Float.parseFloat(attrs.get(5));
        float fuel = Float.parseFloat(attrs.get(9));
        String id = attrs.get(10);
        String lane = attrs.get(11);
        float noise = Float.parseFloat(attrs.get(12));
        String type = attrs.get(16);
        boolean waiting = ((int)Float.parseFloat(attrs.get(17)) == 1);
        float x = Float.parseFloat(attrs.get(18));
        float y = Float.parseFloat(attrs.get(19));

        return new DataEntryDTO(time, co, co2, hc, nox, pmx, fuel, id, lane, noise, type, waiting, x, y);
    }
    public static void produceEvent(KafkaProducer<String, DataEntryDTO> producer, String topic, String lineCsv){

        DataEntryDTO dataObj = toDTOObject(lineCsv);
        ProducerRecord<String, DataEntryDTO> rec = new ProducerRecord<>(topic, dataObj.getKey(), dataObj);
        producer.send(rec);
        System.out.println("New event. Event key(Vehicle ID) = " + dataObj.getKey());
    }


    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Missing input parameters...");
            System.exit(1);
        }
        String inputPath = args[0];
        String topic = args[1];

        try (KafkaProducer<String, DataEntryDTO> producer = createProducer()) {
            File f = new File(inputPath + "/emission_data.csv");
            try (FileReader fr = new FileReader(f)) {
                try(BufferedReader rd = new BufferedReader(fr)){
                    //skipping 1st line
                    rd.readLine();
                    String line = rd.readLine();
                    while(line != null){
                        produceEvent(producer, topic, line);
                        line = rd.readLine();
                        Thread.sleep(1000);
                    }
                    producer.close();
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
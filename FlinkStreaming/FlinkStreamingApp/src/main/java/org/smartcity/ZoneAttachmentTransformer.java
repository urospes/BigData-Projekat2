package org.smartcity;

import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.smartcity.dtos.MessageDTO;
import org.smartcity.dtos.TransformedRecord;

public class ZoneAttachmentTransformer extends ProcessFunction<MessageDTO, TransformedRecord> {

    public float centerX = 23.726766f;
    public float centerY = 37.984473f;

    public String ZoneFunction(float lat, float lng, float center_lat, float center_lng){
        if (lat > center_lat) {
            if (lng > center_lng)
                return "NE";
            else
                return "NW";
        }
        else {
            if(lng > center_lng)
                return "SE";
            else
                return "SW";
        }
    }

    @Override
    public void processElement(MessageDTO record, ProcessFunction<MessageDTO, TransformedRecord>.Context context, Collector<TransformedRecord> collector) throws Exception {
        String zone = ZoneFunction(record.get_vehicleX(), record.get_vehicleY(), centerX, centerY);

        TransformedRecord newRecord = new TransformedRecord(record);
        newRecord.set_zone(zone);

        collector.collect(newRecord);
    }
}

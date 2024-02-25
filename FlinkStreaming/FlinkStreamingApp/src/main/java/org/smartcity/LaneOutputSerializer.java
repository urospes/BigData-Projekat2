package org.smartcity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.smartcity.dtos.FullLaneOutputRecord;

public class LaneOutputSerializer implements SerializationSchema<FullLaneOutputRecord> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(FullLaneOutputRecord record) {
        try {
            return mapper.writeValueAsBytes(record);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

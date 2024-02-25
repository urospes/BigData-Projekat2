package org.smartcity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.smartcity.dtos.FullPollutionOutputRecord;

public class PollutionOutputSerializer implements SerializationSchema<FullPollutionOutputRecord> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(FullPollutionOutputRecord record) {
        try {
            return mapper.writeValueAsBytes(record);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

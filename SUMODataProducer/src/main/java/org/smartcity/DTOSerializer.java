package org.smartcity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DTOSerializer implements Serializer<DataEntryDTO> {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String key, DataEntryDTO dataObj) {
        try {
            if (dataObj == null){
                System.out.println("Null received at serializing.");
                return null;
            }
            return objectMapper.writeValueAsString(dataObj).getBytes(StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing DTO object to byte[]");
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}

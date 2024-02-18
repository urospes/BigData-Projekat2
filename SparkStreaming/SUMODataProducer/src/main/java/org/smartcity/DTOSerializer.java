package org.smartcity;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DTOSerializer implements Serializer<DataEntryDTO> {
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
            return SerializationUtils.serialize(dataObj);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing DTO object to byte[]");
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}

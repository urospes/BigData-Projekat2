package org.smartcity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.smartcity.dtos.MessageDTO;

import java.io.IOException;

public class MessageDeserializer implements DeserializationSchema<MessageDTO> {

    static ObjectMapper mapper = new ObjectMapper();

    @Override
    public TypeInformation<MessageDTO> getProducedType() {
        return TypeInformation.of(MessageDTO.class);
    }

    @Override
    public MessageDTO deserialize(byte[] bytes) throws IOException {
        return mapper.readValue(bytes, MessageDTO.class);
    }

    @Override
    public boolean isEndOfStream(MessageDTO messageDTO) {
        return false;
    }
}

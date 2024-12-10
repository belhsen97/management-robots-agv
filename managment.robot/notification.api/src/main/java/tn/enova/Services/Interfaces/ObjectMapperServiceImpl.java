package tn.enova.Services.Interfaces;

import tn.enova.Services.ObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service("object-mapper-service")
@RequiredArgsConstructor
public class ObjectMapperServiceImpl implements ObjectMapperService {
    private final ObjectMapper objectMapper;
    @Override
    public <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
            return objectMapper.readValue(json, clazz);
    }
    @Override
    public String toJson(Object obj) throws JsonProcessingException {
            return objectMapper.writeValueAsString(obj);
    }
}
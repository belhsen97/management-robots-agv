package com.enova.collector.api.Services.Interfaces;


import com.enova.collector.api.Services.ObjectMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapperServiceImpl implements ObjectMapperService {

    private final ObjectMapper objectMapper;

    public ObjectMapperServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
            return objectMapper.readValue(json, clazz);
    }
    @Override
    public String toJson(Object obj) throws JsonProcessingException {
            return objectMapper.writeValueAsString(obj);
    }
}
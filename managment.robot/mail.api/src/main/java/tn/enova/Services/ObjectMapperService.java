package tn.enova.Services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ObjectMapperService {
    <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException;
    String toJson(Object obj) throws JsonProcessingException;
}

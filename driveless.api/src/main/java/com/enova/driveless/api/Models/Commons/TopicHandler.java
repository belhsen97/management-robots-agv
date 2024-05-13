package com.enova.driveless.api.Models.Commons;

import java.lang.reflect.Method;

public class TopicHandler {
    private String topic;
    private Method method;
    private Object instance;

    public String getTopic() {
        return topic;
    }
    public Method getMethod() {
        return method;
    }

    public Object getInstance() {
        return instance;
    }
    public TopicHandler(String topic, Method method, Object instance) {
        this.topic = topic;
        this.method = method;
        this.instance = instance;
    }
}
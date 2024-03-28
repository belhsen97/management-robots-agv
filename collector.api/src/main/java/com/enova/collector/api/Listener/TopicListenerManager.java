package com.enova.collector.api.Listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TopicListenerManager {
    private final Map<String, Method> topicHandlers;

    public TopicListenerManager(ApplicationContext applicationContext) {
        topicHandlers = new HashMap<>();


        final String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> beanType = applicationContext.getType(beanName);
            if (beanType != null) {
               Method[] methods = beanType.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(ListenerTopic.class)) {
                        ListenerTopic annotation = method.getAnnotation(ListenerTopic.class);
                        topicHandlers.put(annotation.topic(), method);
                    }
                }
            }
        }
    }
    public void handleMessage(String topic, byte[] msg) {
        Method method = topicHandlers.get(topic);
        if (method == null) {
            return;
        }
        try {
            method.invoke(this, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





//        Arrays.stream(applicationContext.getBeanDefinitionNames())
//                .map(applicationContext::getType)
//                .filter(beanType -> beanType != null)
//                .forEach(beanType -> Arrays.stream(beanType.getDeclaredMethods())
//                        .filter(method -> method.isAnnotationPresent(ListenerTopic.class))
//                        .forEach(method -> {
//                            ListenerTopic annotation = method.getAnnotation(ListenerTopic.class);
//                            topicHandlers.put(annotation.topic(), method);
//                        }));
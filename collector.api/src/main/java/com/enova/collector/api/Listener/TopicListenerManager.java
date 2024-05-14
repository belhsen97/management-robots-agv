package com.enova.collector.api.Listener;

import com.enova.collector.api.Models.Commons.Subscribe;
import com.enova.collector.api.Models.Commons.TopicHandler;
import com.enova.collector.api.Services.MQTTService;
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





import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

@Component
public class TopicListenerManager {
    private final List<TopicHandler> topicHandlers;
    public TopicListenerManager(ApplicationContext applicationContext, MQTTService mqttService) {

        topicHandlers = new ArrayList<TopicHandler>();

        final String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.equals("topicListenerManager")||beanName.equals("callback") ){continue;}
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanType = bean.getClass();
            //Class<?> beanType = applicationContext.getType(beanName);
            if (beanType == null) { continue; }
            Method[] methods = beanType.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(ListenerTopic.class)) {
                    continue;
                }
                ListenerTopic annotation = method.getAnnotation(ListenerTopic.class);
                try {
                    mqttService.subscribe(Subscribe.builder().topic(annotation.topic()).qos(annotation.qos()).build());
                    topicHandlers.add(new TopicHandler(annotation.topic(), method , bean));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleMessageArrived(String topic, byte[] msg) {
        /*List<Method> filterMethods = getMethodsForTopic(topicHandlers, topic);
        System.out.println(filterMethods.size());
        if (filterMethods.isEmpty()) {
            return;
        }
        for (Method method : filterMethods) {
            try {
                method.invoke(this, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        List<TopicHandler>filtered =  filterByTopic(topicHandlers, topic);
        if (filtered.isEmpty()) {  return; }
        for (TopicHandler topicHandlers : filtered) {
            try {
                topicHandlers.getMethod().invoke(topicHandlers.getInstance(),topic, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static List<TopicHandler> filterByTopic(List<TopicHandler> topicHandlers, String desiredTopic) {
        List<TopicHandler> filteredList = new ArrayList<>();
        for (TopicHandler handler : topicHandlers) {
            if (handler.getTopic().equals(desiredTopic) || match(desiredTopic, handler.getTopic())) {
                filteredList.add(handler);
            }
        }
        return filteredList;
    }
    public static List<Method> getMethodsForTopic(List<TopicHandler> topicHandlers, String desiredTopic) {
        List<Method> methods = new ArrayList<>();
        for (TopicHandler handler : topicHandlers) {
            if (handler.getTopic().equals(desiredTopic) || match(desiredTopic, handler.getTopic())) {
                methods.add(handler.getMethod());
            }
        }
        return methods;
    }

    public static String topicAllowed(String topic, Map<String, Method> topicHandlers) {
        for (String pattern : topicHandlers.keySet()) {
            if (match(topic, pattern)) {
                return pattern;
            }
        }
        return null;
    }

    private static boolean match(String topic, String pattern) {
        String[] topicParts = topic.split("/");
        String[] patternParts = pattern.split("/");

        int topicLength = topicParts.length;
        int patternLength = patternParts.length;

        if (pattern.endsWith("/#")) {
            patternLength--;
            if (topicLength < patternLength) {
                return false;
            }
        } else {
            if (topicLength != patternLength) {
                return false;
            }
        }

        for (int i = 0; i < patternLength; i++) {
            if (!matchParts(topicParts[i], patternParts[i])) {
                return false;
            }
        }
        return true;
    }
    private static boolean matchParts(String topicPart, String patternPart) {
        if (patternPart.equals("#")) {
            return true;
        } else if (patternPart.equals("+")) {
            return !topicPart.contains("/");
        } else {
            return topicPart.equals(patternPart);
        }
    }
}
























//@Component
//public class TopicListenerManager {
//    private final Map<String, Method> topicHandlers;
//
//    public TopicListenerManager(ApplicationContext applicationContext) {
//        topicHandlers = new HashMap<>();
//
//
//        final String[] beanNames = applicationContext.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            Class<?> beanType = applicationContext.getType(beanName);
//            if (beanType != null) {
//               Method[] methods = beanType.getDeclaredMethods();
//                for (Method method : methods) {
//                    if (method.isAnnotationPresent(ListenerTopic.class)) {
//                        ListenerTopic annotation = method.getAnnotation(ListenerTopic.class);
//                        topicHandlers.put(annotation.topic(), method);
//                    }
//                }
//            }
//        }
//    }
//    public void handleMessage(String topic, byte[] msg) {
//        Method method = topicHandlers.get(topic);
//        if (method == null) {
//            return;
//        }
//        try {
//            method.invoke(this, msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//        Arrays.stream(applicationContext.getBeanDefinitionNames())
//                .map(applicationContext::getType)
//                .filter(beanType -> beanType != null)
//                .forEach(beanType -> Arrays.stream(beanType.getDeclaredMethods())
//                        .filter(method -> method.isAnnotationPresent(ListenerTopic.class))
//                        .forEach(method -> {
//                            ListenerTopic annotation = method.getAnnotation(ListenerTopic.class);
//                            topicHandlers.put(annotation.topic(), method);
//                        }));
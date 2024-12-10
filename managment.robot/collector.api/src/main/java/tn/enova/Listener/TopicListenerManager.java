package tn.enova.Listener;

import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tn.enova.Models.Commons.Subscribe;
import tn.enova.Models.Commons.TopicHandler;
import tn.enova.Services.MQTTService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TopicListenerManager implements ApplicationListener<ApplicationReadyEvent> /*using  implements ApplicationListener<ApplicationReadyEvent> or  @EventListener(ApplicationReadyEvent.class)*/{
    private  List<TopicHandler> topicHandlers;
    private final ApplicationContext applicationContext;
    private final MQTTService mqttService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initializeTopicListeners();
    }


    public void initializeTopicListeners( ) {
        this.topicHandlers = new ArrayList<TopicHandler>();
        final String[] beanNames =  this.applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.equals("topicListenerManager")||beanName.equals("callback") ){
                continue;
            }
            Object bean =  this.applicationContext.getBean(beanName);
            Class<?> beanType = bean.getClass();
            //Class<?> beanType =  this.applicationContext.getType(beanName);
            if (beanType == null) { continue; }
            Method[] methods = beanType.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(ListenerTopic.class)) {
                    continue;
                }
                ListenerTopic annotation = method.getAnnotation(ListenerTopic.class);
                try {
                    this.mqttService.subscribe(Subscribe.builder().topic(annotation.topic()).qos(annotation.qos()).build());
                    this.topicHandlers.add(new TopicHandler(annotation.topic(), method , bean));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleMessageArrived(String topic, byte[] msg) {
        List<TopicHandler>filtered =  filterByTopic(topicHandlers, topic);
        if (filtered.isEmpty()) {  return; }
        for (TopicHandler handler : filtered) {
            try {
                handler.getMethod().invoke(handler.getInstance(),topic, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private static List<TopicHandler> filterByTopic(List<TopicHandler> topicHandlers, String desiredTopic) {
        List<TopicHandler> filteredList = new ArrayList<>();
        for (TopicHandler handler : topicHandlers) {
            if (handler.getTopic().equals(desiredTopic) || match(desiredTopic, handler.getTopic())) {
                filteredList.add(handler);
            }
        }
        return filteredList;
    }
    private static List<Method> getMethodsForTopic(List<TopicHandler> topicHandlers, String desiredTopic) {
        List<Method> methods = new ArrayList<>();
        for (TopicHandler handler : topicHandlers) {
            if (handler.getTopic().equals(desiredTopic) || match(desiredTopic, handler.getTopic())) {
                methods.add(handler.getMethod());
            }
        }
        return methods;
    }

    private static String topicAllowed(String topic, Map<String, Method> topicHandlers) {
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
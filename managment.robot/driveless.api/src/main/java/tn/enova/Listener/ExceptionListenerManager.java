package tn.enova.Listener;


import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tn.enova.Models.Commons.ExceptionHandler;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ExceptionListenerManager  implements  ApplicationListener<ApplicationReadyEvent> {
    private List<ExceptionHandler> exceptionHandlers;
    private final ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initializeHandlers();
    }

    public void initializeHandlers() {
        this.exceptionHandlers = new ArrayList<>();
        final String[] beanNames = this.applicationContext.getBeanDefinitionNames();

        // Iterate through all beans in the application context
        for (String beanName : beanNames) {
            if (beanName.equals("exceptionHandlerManager")) {
                continue;
            }

            Object bean = this.applicationContext.getBean(beanName);
            Class<?> beanType = bean.getClass();
            Method[] methods = beanType.getDeclaredMethods();

            // Check each method for the @CustomExceptionHandler annotation
            for (Method method : methods) {
                if (method.isAnnotationPresent(CustomExceptionHandler.class)) {
                    CustomExceptionHandler annotation = method.getAnnotation(CustomExceptionHandler.class);
                    this.exceptionHandlers.add(new ExceptionHandler(annotation.value(), method, bean));
                }
            }
        }
    }

    // Method to call the correct handler when an exception occurs
    public void handleException(Throwable exception) {
        if (exceptionHandlers == null || exceptionHandlers.isEmpty()) {
            System.err.println("Exception handlers not initialized or empty.");
            return;
        }
        for (ExceptionHandler handler : exceptionHandlers) {
            if (handler.getExceptionType().isAssignableFrom(exception.getClass())) {
                try {
                    // Invoke the method with the exception as an argument
                    handler.getMethod().invoke(handler.getInstance(), exception);
                } catch (Exception e) {
                    e.printStackTrace(); // Handle reflection invocation exceptions
                }
            }
        }
    }
}

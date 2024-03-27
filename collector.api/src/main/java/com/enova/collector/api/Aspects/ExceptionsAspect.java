package com.enova.collector.api.Aspects;



import com.enova.collector.api.Exceptions.RessourceNotFoundException;
import com.enova.collector.api.Services.MQTTService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ExceptionsAspect {

//    @AfterThrowing(pointcut = "execution(* com.enova.collector.api.Services.*.*(..))",throwing = "error")
//    public void logMethodThrowing(JoinPoint joinPoint,Throwable error) {
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//      System.out.println(  "AfterThrowing class: " + className+" - method: " + methodName+" - Message: " + error);
//    }



    @AfterThrowing(pointcut = "execution(* com.enova.collector.api.Services.*.*(..))", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Throwable ex) {
        if (ex instanceof RessourceNotFoundException) {
            // Handle MyCustomException
            System.out.println("Handling MyCustomException: " + ex.getMessage());
            // You can perform logging, send notifications, etc.
        } else {
            // Handle other exceptions
            System.out.println("Handling other exception: " + ex.getMessage());
            // You can have separate handling for different exception types
        }
    }

}
/*
            final  MsgReponseStatusService msg = MsgReponseStatusService.childBuilder()
                    .status(ReponseStatus.ERROR)
                    .build();
            try {
                mqttService.publish("exception-services" , msg);
            } catch (MqttException ex) {
                throw new RuntimeException(ex);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
* */
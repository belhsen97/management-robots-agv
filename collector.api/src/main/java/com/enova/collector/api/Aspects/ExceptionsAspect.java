package com.enova.collector.api.Aspects;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionsAspect {
    @AfterThrowing(pointcut = "execution(* com.enova.collector.api.Services.*.*(..))",throwing = "error")
    public void logMethodThrowing(JoinPoint joinPoint,Throwable error) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
      System.out.println(  "AfterThrowing class: " + className+" - method: " + methodName+" - Message: " + error);
    }
}

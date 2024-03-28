package com.enova.collector.api.Aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionsAspect {
    @AfterThrowing(pointcut = "execution(* com.enova.collector.api.AppRunners.*.*(..))", throwing = "error")
    public void logAppRunnersThrowing(JoinPoint joinPoint, Throwable error) {//if (ex instanceof RessourceNotFoundException)
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.error("AfterThrowing class: " + className + " - method: " + methodName + " - Message: " + error);
    }

    @AfterThrowing(
            pointcut = "execution(* com.enova.collector.api.Callbacks.*.*(..)) "//|| " +
            // "execution(* com.enova.collector.api.Services.*.*(..))",
            , throwing = "error"
    )
    public void logException(JoinPoint joinPoint, Throwable error) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        final String packageType = (className.startsWith("com.enova.collector.api.Callbacks") ? "Callback" : "");
        log.error("Exception in " + packageType + ": Class: " + className + " - Method: " + methodName + " - Message: " + error);
    }
}
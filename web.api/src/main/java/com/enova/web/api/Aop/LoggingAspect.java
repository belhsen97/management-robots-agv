package com.enova.web.api.Aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("execution(* com.enova.web.api.Controllers.*.*(*))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Method method = getMethod(joinPoint);
        String[] paramNames = getParameterNames(method);
        StringBuilder argString = new StringBuilder();
        argString.append("in class: ").append(className).append(" - method: ").append(methodName).append(" Arguments: [");
        for (int i = 0; i < args.length; i++) {
            argString.append(paramNames[i]).append("=").append(args[i]);
            if (i < args.length - 1) {
                argString.append(", ");
            }
        }
        argString.append("]");
        log.info(argString.toString());
    }

    private Method getMethod(JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String methodName = joinPoint.getSignature().getName();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Method not found: " + methodName);
    }
    private String[] getParameterNames(Method method) {
        String[] paramNames = new String[method.getParameterCount()];
        int i = 0;
        for (var param : method.getParameters()) {
            paramNames[i++] = param.getName();
        }
        return paramNames;
    }

//    @After(" execution(* com.enova.web.api.Services.*.*(..)) ")
//    public void logMethodExit(JoinPoint joinPoint) {
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        log.info(  "out class: " + className+" - method: " + methodName );
//    }
//    @AfterReturning(" execution(* com.enova.web.api.Services.*.*(..)) ")
//    public void logMethodReturning(JoinPoint joinPoint) {
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        log.info(  "AfterReturning class: " + className+" - method: " + methodName );
//    }
    @AfterThrowing(pointcut = "execution(* com.enova.web.api.Services.*.*(..))",throwing = "error")
    public void logMethodThrowing(JoinPoint joinPoint,Throwable error) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        log.error(  "AfterThrowing class: " + className+" - method: " + methodName+" - Message: " + error);
    }
}

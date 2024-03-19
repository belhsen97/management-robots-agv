package com.enova.web.api.Aop;

//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
public class TraceAspect  {
/*
   // private static final Logger logger = LoggerFactory.getLogger(TraceAspect.class);

    @Before("@annotation(Trace)")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        final String methodName = joinPoint.getSignature().getName();
        final String username = getUsername();
       // logger.info("Method '{}' called by user: {}", methodName, username);
        System.out.println("Method '{}' called by user: {}"+ methodName+"-"+ username);

        // Get method signature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Get class name
        String className = joinPoint.getTarget().getClass().getName();

        // Get method name
        String methodName2 = signature.getName();

        // Get method arguments
        Object[] args = joinPoint.getArgs();
        System.out.println("className: " + className+ " methodName2 "+methodName2);
        // Process method arguments if needed
        // Example: Print method arguments
        for (Object arg : args) {
            System.out.println("Argument: " + arg.toString());
        }
    }

    @AfterReturning("@annotation(Trace)")
    public void afterMethodExecution(JoinPoint joinPoint) {
        final String methodName = joinPoint.getSignature().getName();
        final String username = getUsername();
      //  logger.info("Method '{}' execution completed", methodName);
        System.out.println("Method '"+methodName+"' execution completed-"+ username);
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) { return authentication.getName();}
        return "Unknown";
    }*/
}
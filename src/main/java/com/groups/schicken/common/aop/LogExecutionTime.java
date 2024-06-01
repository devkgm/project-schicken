package com.groups.schicken.common.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component
@Aspect
public class LogExecutionTime {
    Logger logger =  LoggerFactory.getLogger(LogExecutionTime.class);
    @Around("execution(* com.groups.schicken..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info(joinPoint.getSignature().getName() + " execution time : " + (endTime - startTime) + "ms");
        return result;
    }
}

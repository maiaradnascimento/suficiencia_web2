package br.edu.utfpr.server.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Aspect
@Component
public class DatabaseLoggingAspect {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Around("execution(* br.edu.utfpr.server.repository.*.*(..))")
    public Object logRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        String operation = getOperationType(methodName);
        
        log.info("[{}] DB {} - {}.{}() with {} parameters", 
                timestamp, operation, className, methodName, args.length);
        
        if (args.length > 0) {
            log.debug("Parameters: {}", java.util.Arrays.toString(args));
        }
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("[{}] DB {} SUCCESS - {}.{}() - Time: {}ms", 
                    timestamp, operation, className, methodName, duration);
            
            if (result != null) {
                logResult(result, operation);
            }
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("[{}] DB {} ERROR - {}.{}() - Time: {}ms - Error: {}", 
                    timestamp, operation, className, methodName, duration, e.getMessage());
            throw e;
        }
    }

    private String getOperationType(String methodName) {
        if (methodName.startsWith("save") || methodName.startsWith("insert") || 
            methodName.startsWith("create") || methodName.startsWith("persist")) {
            return "INSERT";
        } else if (methodName.startsWith("update") || methodName.startsWith("modify")) {
            return "UPDATE";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "DELETE";
        } else if (methodName.startsWith("find") || methodName.startsWith("get") || 
                   methodName.startsWith("select") || methodName.startsWith("exists") ||
                   methodName.startsWith("count")) {
            return "SELECT";
        } else {
            return "OPERATION";
        }
    }


    private void logResult(Object result, String operation) {
        if (result instanceof java.util.Collection<?> collection) {
            log.info("Result {} returned {} records", operation, collection.size());
        } else if (result instanceof java.util.Optional<?> optional) {
            log.info("Result {} returned: {}", operation, optional.isPresent() ? "1 record" : "no records");
        } else if (result instanceof Number number) {
            log.info("Result {} returned: {}", operation, number);
        } else {
            log.info("Result {} returned: {}", operation, result.getClass().getSimpleName());
        }
    }
}

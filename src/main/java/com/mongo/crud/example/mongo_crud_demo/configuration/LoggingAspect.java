package com.mongo.crud.example.mongo_crud_demo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect
{

    @Before("execution(public * com.mongo.crud.example.mongo_crud_demo.controller.MongoController.get*(..))")
    public void logBeforeCallMethod(JoinPoint joinPoint){
        log.info("Method {} Before method call ----------------------> ",joinPoint.getSignature().getName());
    }
}

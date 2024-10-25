package com.pp.userservice.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// start L4 AspectJ - Timing
@Aspect
@Slf4j
@Component
public class TimingAspect {

  @Around("execution(* com.pp.userservice.user.UserServiceImplementation.*(..))")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = joinPoint.proceed(); // Wykonaj metodę
    long elapsedTime = System.currentTimeMillis() - start;
    log.info("Execution time of {}: {} ms", joinPoint.getSignature().getName(), elapsedTime);
    return result;
  }
}

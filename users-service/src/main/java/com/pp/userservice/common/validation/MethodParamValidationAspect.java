package com.pp.userservice.common.validation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// start L4 AspectJ - Validation
@Aspect
@Order(1)  // Highest priority if multiple aspects
@Component
public class MethodParamValidationAspect {

  @Before("execution(* com.pp.userservice.notification.NotificationService.*(..))")

  public void validateParams(JoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();

    // Retrieve method parameters and annotations
    Object[] args = joinPoint.getArgs();
    Parameter[] parameters = method.getParameters();

    List<String> errorMessages = new ArrayList<>();

    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      Object argValue = args[i];

      // Check for @NotNull annotation
      if (parameter.isAnnotationPresent(NotNullParam.class)
          || parameter.isAnnotationPresent(NotEmptyParam.class)
          || parameter.isAnnotationPresent(NotBlankParam.class)) {
        if (argValue == null) {
          errorMessages.add(String.format("Parameter %s cannot be null", parameter.getName()));
        }
      }

      // Check for @NotEmpty annotation (only applies to Strings)
      if (parameter.isAnnotationPresent(NotEmptyParam.class) && argValue instanceof String) {
        if (((String) argValue).isEmpty()) {
          errorMessages.add(String.format("Parameter %s cannot be empty", parameter.getName()));
        }
      }

      // Check for @NotEmpty annotation (only applies to Strings)
      if (parameter.isAnnotationPresent(NotBlankParam.class) && argValue instanceof String) {
        if (((String) argValue).isBlank()) {
          errorMessages.add(String.format("Parameter %s cannot be blank", parameter.getName()));
        }
      }
    }

    if (CollectionUtils.isNotEmpty(errorMessages)) {
      throw new IllegalArgumentException(errorMessages.toString());
    }
  }
}

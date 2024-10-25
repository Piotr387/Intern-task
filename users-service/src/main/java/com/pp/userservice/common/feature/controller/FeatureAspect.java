package com.pp.userservice.common.feature.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class FeatureAspect {

  private final FeatureService featureService;

  @Around("@annotation(featureController)")
  public Object checkFeature(ProceedingJoinPoint joinPoint, FeatureController featureController)
      throws Throwable {
    FeatureVersions featureVersion = featureController.value();
    FeatureMode featureMode = featureController.featureMode();

    if (!featureService.isFeatureEnabled(featureVersion)) {
      log.warn("Feature {} is disabled!", featureVersion);

      if (featureMode == FeatureMode.THROW_EXCEPTION) {
        throw new FeatureException(featureVersion);
      } else {
        return null; // Explicitly prevent proceeding
      }
    }

    return joinPoint.proceed();
  }
}

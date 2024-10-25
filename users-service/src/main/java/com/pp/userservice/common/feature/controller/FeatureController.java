package com.pp.userservice.common.feature.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// start L4 AspectJ - feature controller
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureController {
  FeatureVersions value();
  FeatureMode featureMode() default FeatureMode.THROW_EXCEPTION;
}

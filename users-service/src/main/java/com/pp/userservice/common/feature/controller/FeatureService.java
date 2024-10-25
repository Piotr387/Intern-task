package com.pp.userservice.common.feature.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FeatureConfigurator.class)
public class FeatureService {

  private final FeatureConfigurator featureConfigurator;

  public boolean isFeatureEnabled(FeatureVersions featureVersions) {

    return switch (featureVersions) {
      case REGISTER_USER -> featureConfigurator.isRegisterUser();
      case SIGN_FOR_LECTURE -> featureConfigurator.isSignForLecture();
    };
  }
}

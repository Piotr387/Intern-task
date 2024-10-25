package com.pp.userservice.common.feature.controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeatureException extends RuntimeException {

  private final FeatureVersions featureFlag;

  @Override
  public String getMessage() {
    return String.format("Feature %s is disabled on this environment!", featureFlag);
  }
}

package com.pp.userservice.common.feature.controller;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "feature.controller.versions")
public class FeatureConfigurator {

  private boolean registerUser;
  private boolean signForLecture;
}

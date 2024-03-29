package com.pp.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication( //
    scanBasePackages = { //
        "com.pp.userservice", //
        "com.pp.amqp" //
    } //
)
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.pp.messages")
public class UsersApplication {
  public static void main(String[] args) {
    SpringApplication.run(UsersApplication.class, args);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

package com.pp.userservice.config;

import com.pp.amqp.RabbitMQMessageProducer;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class IntegrationTestConfiguration {

  @Bean
  @Primary
  RabbitMQMessageProducer RabbitMQMessageProducer(){

    return Mockito.mock(RabbitMQMessageProducer.class);
  }
}

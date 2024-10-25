package com.pp.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQMessageService {

  private final RabbitMQMessageProducer rabbitMQMessageProducer;

  public void sendEmailRegistrationConfirmRequest(String payload) {
    rabbitMQMessageProducer.publish( //
        payload,
        //
        "internal.exchange", //
        "internal.email.routing-key" //
    );
  }
}

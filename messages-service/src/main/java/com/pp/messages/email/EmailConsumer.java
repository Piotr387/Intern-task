package com.pp.messages.email;

import com.pp.messages.EmailRegistrationConfirmRequest;
import com.pp.messages.EmailSignUpConfirmationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {

  private final EmailService emailService;

  @RabbitListener(queues = "${rabbitmq.queue.email}")
  public void sendEmail(EmailSignUpConfirmationRequest request) {
    log.info("Sending new email in progress {}", request);
    emailService.send(request);
    log.info("Email message has been sent! {}", request);
  }

  @RabbitListener(queues = "${rabbitmq.queue.email}")
  public void sendEmail(EmailRegistrationConfirmRequest request) {
    log.info("Sending new registration email in progress {}", request);
    emailService.sendInvitationEmail(request);
    log.info("Registration Email message has been sent! {}", request);
  }
}

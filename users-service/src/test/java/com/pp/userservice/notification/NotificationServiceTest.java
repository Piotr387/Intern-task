package com.pp.userservice.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pp.userservice.CommonTest;
import com.pp.userservice.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

// start L4 test 4
@ExtendWith(OutputCaptureExtension.class)
class NotificationServiceTest extends CommonTest {

  private final String TEST_MESSAGE_CONTENT = "Testing Notification Service";
  @Autowired
  private NotificationService notificationService;

  @Test
  void sendNotification_whenOnlySMSNotificationIsEnabled_SMSNotifierShouldBeLaunch(
      CapturedOutput output) {

    // given
    UserEntity userEntity = createUserEntity(true, false, false);

    // when
    notificationService.sendNotification(userEntity, TEST_MESSAGE_CONTENT);

    // then
    assertThat(output).contains("Sending SMS message");
    assertThat(output).doesNotContain("Sending notification to facebook!");
    assertThat(output).doesNotContain(
        "Sending Notification to Slack application with content " + TEST_MESSAGE_CONTENT);
    assertThat(output).contains("Message was successfully send!");
  }

  @Test
  void sendNotification_whenOnlyFacebookNotificationIsEnabled_FacebookNotifierShouldBeLaunch(
      CapturedOutput output) {

    // given
    UserEntity userEntity = createUserEntity(false, true, false);

    // when
    notificationService.sendNotification(userEntity, TEST_MESSAGE_CONTENT);

    // then
    assertThat(output).doesNotContain("Sending SMS message");
    assertThat(output).contains("Sending notification to facebook!");
    assertThat(output).doesNotContain(
        "Sending Notification to Slack application with content " + TEST_MESSAGE_CONTENT);
    assertThat(output).contains("Message was successfully send!");
  }

  @Test
  void sendNotification_whenOnlySlackNotificationIsEnabled_SlackNotifierShouldBeLaunch(
      CapturedOutput output) {

    // given
    UserEntity userEntity = createUserEntity(false, false, true);

    // when
    notificationService.sendNotification(userEntity, TEST_MESSAGE_CONTENT);

    // then
    assertThat(output).doesNotContain("Sending SMS message");
    assertThat(output).doesNotContain("Sending notification to facebook!");
    assertThat(output).contains(
        "Sending Notification to Slack application with content " + TEST_MESSAGE_CONTENT);
    assertThat(output).contains("Message was successfully send!");
  }

  @Test
  void sendNotification_whenAllNotificationIsEnabled_AllNotifierShouldBeLaunch(
      CapturedOutput output) {

    // given
    UserEntity userEntity = createUserEntity(true, true, true);

    // when
    notificationService.sendNotification(userEntity, TEST_MESSAGE_CONTENT);

    // then
    assertThat(output).contains("Sending SMS message");
    assertThat(output).contains("Sending notification to facebook!");
    assertThat(output).contains(
        "Sending Notification to Slack application with content " + TEST_MESSAGE_CONTENT);
    assertThat(output).contains("Message was successfully send!");
  }

  @Test
  void sendNotification_whenNotificationsAreDisabled_OnlyDefaultShouldBeLaunched(
      CapturedOutput output) {

    // given
    UserEntity userEntity = new UserEntity();

    // when
    notificationService.sendNotification(userEntity, TEST_MESSAGE_CONTENT);

    // then
    assertThat(output).doesNotContain("Sending SMS message");
    assertThat(output).doesNotContain("Sending notification to facebook!");
    assertThat(output).doesNotContain(
        "Sending Notification to Slack application with content " + TEST_MESSAGE_CONTENT);
    assertThat(output).contains("Message was successfully send!");
  }

  @ParameterizedTest
  @MethodSource("sendNotificationWhenNotAllowedValuesArePassedExceptionShouldBeThrown")
  void sendNotification_whenNotAllowedValuesArePassed_ExceptionShouldBeThrown(UserEntity userEntity,
      String message, String expectedMessage) {

    // when & then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      notificationService.sendNotification(userEntity, message);
    });

    // Assert the exception message matches the expected message
    assertEquals(expectedMessage, exception.getMessage());
  }

  private static Object[][] sendNotificationWhenNotAllowedValuesArePassedExceptionShouldBeThrown() {

    return new Object[][]{
        {null, "Message", "[Parameter arg0 cannot be null]"}, //
        {new UserEntity(), null, "[Parameter arg1 cannot be null]"}, //
        {new UserEntity(), "", "[Parameter arg1 cannot be empty, Parameter arg1 cannot be blank]"}, //
        {new UserEntity(), " ", "[Parameter arg1 cannot be blank]"} //
    };
  }


  private static UserEntity createUserEntity(boolean SMSNotification, boolean facebookNotification,
      boolean slackNotification) {

    UserEntity userEntity = new UserEntity();
    userEntity.setSMSNotification(SMSNotification);
    userEntity.setFacebookNotification(facebookNotification);
    userEntity.setSlackNotification(slackNotification);

    return userEntity;
  }
}
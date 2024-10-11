package com.pp.userservice.user;

import static com.pp.userservice.response.ErrorMessages.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.amqp.RabbitMQMessageProducer;
import com.pp.userservice.IntegrationBaseTest;
import com.pp.userservice.lecture.dto.LectureSignUpDTO;
import com.pp.userservice.response.ErrorMessages;
import com.pp.userservice.response.OperationStatusModel;
import com.pp.userservice.response.RequestOperationName;
import com.pp.userservice.user.api.UserController;
import com.pp.userservice.user.api.UserDTO;
import com.pp.userservice.utils.Utilities;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

class UserControllerTest extends IntegrationBaseTest {

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private Utilities utilities;
  @MockBean
  private RabbitMQMessageProducer rabbitMQMessageProducer;
  private static final String NEW_EMAIL = "newUser@gmail.com";;
  private static final String NEW_USER = "newUser";
  private static final String ROLE_USER = "ROLE_USER";
  private static final String ROLE_ORGANIZER = "ROLE_ORGANIZER";

  @Test
  void signUpUserForLecture_whenProvidedCorrectRequestAndFreeSeatsAreAvaible_ReturnOk() throws Exception {

    // given
    var url = createUrl("/sign-up");
    var lectureSignUpDTO = new LectureSignUpDTO("Lecture 1 at 12:00", new UserDTO(NEW_USER, NEW_EMAIL));
    var request = objectMapper.writeValueAsString(lectureSignUpDTO);

    // when
    var response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
        .content(request))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    // then
    var expectedResponse = new OperationStatusModel.Builder(RequestOperationName.SIGN_UP_FOR_LECTURE.name()).build();
    var expectedResponseAsString = objectMapper.writeValueAsString(expectedResponse);
    assertEquals(expectedResponseAsString, response);

  }

  @ParameterizedTest
  @MethodSource("signUpUserForLectureWhenProvidedCorrectRequestAndVariousCombinationThatReturnError")
  void signUpUserForLecture_whenProvidedCorrectRequestAndVariousCombinationThatReturnError_Returns4xx(ErrorMessages errorMessages,
      LectureSignUpDTO lectureSignUpDTO) throws Exception {

    // given
    var url = createUrl("/sign-up");
    var request = objectMapper.writeValueAsString(lectureSignUpDTO);

    // when & then
    var response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
        .content(request))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResolvedException();

    assertNotNull(response);
    assertEquals(getMessage(errorMessages), response.getMessage());
  }

  private static Object[][] signUpUserForLectureWhenProvidedCorrectRequestAndVariousCombinationThatReturnError() {

    return new Object[][] { //
        {NO_FREE_SEATS_AT_LECTURE, new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO(NEW_USER, NEW_EMAIL))}, //
        {LOGIN_ALREADY_TAKEN, new LectureSignUpDTO("Lecture 1 at 12:00", new UserDTO("test", NEW_EMAIL))}, //
        {EMAIL_ALREADY_TAKEN, new LectureSignUpDTO("Lecture 1 at 12:00", new UserDTO(NEW_USER, "test@test.com"))}, //
        {LOGIN_MISS_PATTERN, new LectureSignUpDTO("Lecture 1 at 12:00", new UserDTO("ne", NEW_EMAIL))}, //
        {LOGIN_MISS_PATTERN, new LectureSignUpDTO("Lecture 1 at 12:00", new UserDTO("new#$", NEW_EMAIL))}, //
        {EMAIL_MISS_PATTERN, new LectureSignUpDTO("Lecture 1 at 12:00", new UserDTO(NEW_USER, "test"))}, //
        {LECTURE_NOT_FOUND_BY_NAME, new LectureSignUpDTO("", new UserDTO(NEW_USER, NEW_EMAIL))}, //
    };
  }

  @ParameterizedTest
  @MethodSource("loginWhenProvidedCorrectLoginAndPasswordAndExpectedRoleOfUser")
  void login_whenProvidedCorrectLoginAndPassword_ReturnOkWithAccessTokenAndRefreshTokenWithExpectedRoleInToken(String login, String password,
      List<String> roles) throws Exception {

    // given
    var url = createUrl("login");

    // when & then
    var response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("login", login)
        .param("password", password))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(2)))
        .andReturn()
        .getResponse()
        .getContentAsString();

    LoginTestResponse loginResponse = objectMapper.readValue(response, LoginTestResponse.class);
    checkIfUsernameIsTheOwnerOfToken(login, loginResponse.access_token());
    checkIfUsernameIsTheOwnerOfToken(login, loginResponse.refresh_token());
    checkIfTokenContainsExpectedRoles(loginResponse.access_token(), roles);
  }

  private static Object[][] loginWhenProvidedCorrectLoginAndPasswordAndExpectedRoleOfUser() {

    return new Object[][] { //
        {"test", "test", List.of(ROLE_USER)}, //
        {"organizator1", "organizator1", List.of(ROLE_ORGANIZER)}, //
        {"username1", "rpP3rU0tqT9nGyr", List.of(ROLE_USER)}, //
    };
  }

  @Test
  void getNewAccessToken_WhenProvidedCorrectRefreshToken_ReturnOkWithNewAccessTokenAndWithTheSameRefreshToken() throws Exception {

    // given
    var url = createUrl("/token/refresh");
    var refreshToken = utilities.createRefreshToken("test", "/users/token/refresh");

    // when & then
    var response = mockMvc.perform(get(url).servletPath("/users/token/refresh").header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(2)))
        .andReturn()
        .getResponse()
        .getContentAsString();

    LoginTestResponse loginResponse = objectMapper.readValue(response, LoginTestResponse.class);
    checkIfUsernameIsTheOwnerOfToken("test", loginResponse.access_token());
    checkIfUsernameIsTheOwnerOfToken("test", loginResponse.refresh_token());
  }

  @Test
  void getNewAccessToken_WhenProvidedInCorrectRefreshToken_ReturnIsForbidden() throws Exception {

    // given
    var url = createUrl("/token/refresh");
    var expiredRefreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTE3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3VzZXJzL2xvZ2luIiwiZXhwIjoxNjUyMzU4MTU1fQ.psYoe7xJgeMfM9V5POiCaMPgzYzk_pMXaeYVyCIfnkI";

    // when & then
    var response = mockMvc.perform(get(url).servletPath("/users/token/refresh").header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredRefreshToken))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error_message").value("The Token has expired on Thu May 12 14:22:35 CEST 2022."));
  }

  private void checkIfTokenContainsExpectedRoles(String token, List<String> roles) {

    DecodedJWT jwt = JWT.decode(token);
    var rolesInToken = jwt.getClaim("roles")
        .asArray(String.class);
    var rolesInTokenAsList = Arrays.asList(rolesInToken);
    assertEquals(roles, rolesInTokenAsList);
  }

  private void checkIfUsernameIsTheOwnerOfToken(String username, String token) {

    assertEquals(username, utilities.getUserLoginFromToken(token));
  }

  private String getMessage(ErrorMessages errorMessages) {

    return errorMessages.getErrorMessage();
  }

  private static ResultMatcher assertMessage(String expectedMessage) {

    return jsonPath("$.message").value(expectedMessage);
  }

  private String createUrl() {

    return createUrl(null);
  }

  private String createUrl(String url) {

    return UserController.USERS_ENDPOINT + "/" + url;
  }
}

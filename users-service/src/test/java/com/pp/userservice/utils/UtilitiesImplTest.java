package com.pp.userservice.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import com.pp.userservice.response.ErrorMessages;
import com.pp.userservice.response.UserServiceException;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

// start L4 test 1
class UtilitiesImplTest {

  private UtilitiesImpl utilities;
  private HttpServletRequest request;
  private HttpServletResponse response;

  @BeforeEach
  void setUp() {
    utilities = new UtilitiesImpl();
    request = Mockito.mock(HttpServletRequest.class);
    response = Mockito.mock(HttpServletResponse.class);
  }

  @Test
  void testGetEmailRegexPattern() {
    String expectedPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    assertEquals(expectedPattern, utilities.getEmailRegexPattern());
  }

  @Test
  void testGetUsernameRegexPattern() {
    String expectedPattern = "^[a-zA-Z0-9._-]{3,}$";
    assertEquals(expectedPattern, utilities.getUsernameRegexPattern());
  }

  @Test
  void testGenerateRandomString() {
    String randomString = utilities.generateRandomString(10);
    assertEquals(10, randomString.length());
    // Check if it contains valid characters
    assertTrue(randomString.chars().allMatch(
        ch -> "0123456789ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuwxyz".indexOf(ch) != -1));
  }

  @Test
  void testGetUserLoginFromRequest_WithValidToken() {
    String token = JWT.create()
        .withSubject("testUser")
        .sign(utilities.createAlgorithm());

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

    String login = utilities.getUserLoginFromRequest(request);
    assertEquals("testUser", login);
  }

  @Test
  void testGetUserLoginFromRequest_WithMissingToken() {
    when(request.getHeader("Authorization")).thenReturn(null);

    UserServiceException exception = assertThrows(UserServiceException.class, () -> {
      utilities.getUserLoginFromRequest(request);
    });

    assertEquals(ErrorMessages.NO_TOKEN_PROVIDED.getErrorMessage(), exception.getMessage());
  }

  @Test
  void testPatternMatches_WithValidPattern() {
    assertTrue(utilities.patternMatches("valid_username", utilities.getUsernameRegexPattern()));
    assertFalse(
        utilities.patternMatches("ab", utilities.getUsernameRegexPattern())); // Invalid username
  }

  @Test
  void testCreateAccessToken() {
    String token = utilities.createAccessToken("testUser", "http://localhost",
        Collections.emptyList());
    assertNotNull(token);
    assertTrue(token.startsWith("ey")); // JWT tokens typically start with "ey"
  }

  @Test
  void testCreateRefreshToken() {
    String token = utilities.createRefreshToken("testUser", "http://localhost");
    assertNotNull(token);
    assertTrue(token.startsWith("ey")); // JWT tokens typically start with "ey"
  }

  @Test
  void testCreateMapOfTokens() {
    Map<String, String> tokens = utilities.createMapOfTokens("accessToken123", "refreshToken123");
    assertEquals(2, tokens.size());
    assertEquals("accessToken123", tokens.get("access_token"));
    assertEquals("refreshToken123", tokens.get("refresh_token"));
  }
}

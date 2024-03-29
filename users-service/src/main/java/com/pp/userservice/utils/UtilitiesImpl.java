package com.pp.userservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.userservice.responde.ErrorMessages;
import com.pp.userservice.responde.UserServiceException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class UtilitiesImpl implements Utilities {
    private static final Random random = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuwxyz";
    //RFC 5322 standard
    private static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    /**
     * Username pattern:
     * Length >=3
     * Valid characters: a-z, A-Z, 0-9, points, dashes and underscores.
     */
    private static final String USERNAME_REGEX_PATTERN = "^[a-zA-Z0-9._-]{3,}$";

    public String getEmailRegexPattern(){
        return EMAIL_REGEX_PATTERN;
    }

    public String getUsernameRegexPattern() {
        return USERNAME_REGEX_PATTERN;
    }

    /**
     * Function that will generate password for each user that will register, this password will be send via
     * email that he decide to sign up for lecture.
     */
    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    @Override
    public String getUserLoginFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            return getUserLoginFromToken(token);
        }
        throw new UserServiceException(ErrorMessages.NO_TOKEN_PROVIDED.getErrorMessage());
    }

    @Override
    public String getUserLoginFromToken(String token) {
        JWTVerifier verifier = JWT.require(createAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    @Override
    public String generatePassword() {
        return generateRandomString(15);
    }

    @Override
    public Algorithm createAlgorithm() {
        // "secret" is hard code "secret token" which should be taken from some encrypted file
        return Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String createRefreshToken(String login, String requestURL) {
        return JWT.create()
                .withSubject(login)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(requestURL)
                .sign(createAlgorithm());
    }

    public String createAccessTokenForMicroservice(){
        return createAccessToken("techUser", "none", List.of());
    }

    @Override
    public String createAccessToken(String login, String requestURL, List<String> roleList) {
        return JWT.create()
                .withSubject(login)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(requestURL)
                .withClaim("roles", roleList)
                .sign(createAlgorithm());
    }

    @Override
    public Map<String, String> createMapOfTokens(String accessToken, String refreshToken) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    @Override
    public void tokenCreatingException(Exception e, HttpServletResponse response) throws IOException {
        response.setHeader("error", e.getMessage());
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", e.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    @Override
    public boolean patternMatches(String str, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(str)
                .matches();
    }

}

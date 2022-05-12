package com.example.interntask.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class UtilitiesImpl implements Utilities {
    private final Random random = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuwxyz";

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
        throw new RuntimeException("No token provided");
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
        // "secret" is hard code "secret token" wchich should be taken from some ecrypted file
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
    public Map<String, String> createMapOfTokens(String access_token, String refresh_token) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
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

}

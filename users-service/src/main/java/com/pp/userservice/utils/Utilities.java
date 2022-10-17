package com.pp.userservice.utils;

import com.auth0.jwt.algorithms.Algorithm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Utilities {

    String getUserLoginFromRequest(HttpServletRequest request);

    String getUserLoginFromToken(String token);

    String generatePassword();

    //Algorith used over whole project to create/read tokens
    Algorithm createAlgorithm();

    String createRefreshToken(String login, String requestURL);

    String createAccessToken(String username, String toString, List<String> collect);

    Map<String, String> createMapOfTokens(String accessToken, String refreshToken);

    /**
     * This method is to avoid duplication in code, it was used in two places
     * @param e throwns exception while creating token
     * @param response
     */
    void tokenCreatingException(Exception e, HttpServletResponse response) throws IOException;

    boolean patternMatches(String str, String regexPattern);

    String getUsernameRegexPattern();

    String getEmailRegexPattern();
    String createAccessTokenForMicroservice();
}

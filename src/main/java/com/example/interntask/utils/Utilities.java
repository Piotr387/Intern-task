package com.example.interntask.utils;

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

    Algorithm createAlgorithm();

    String createRefreshToken(String login, String requestURL);

    String createAccessToken(String username, String toString, List<String> collect);

    Map<String, String> createMapOfTokens(String access_token, String refresh_token);

    void tokenCreatingException(Exception e, HttpServletResponse response) throws IOException;
}

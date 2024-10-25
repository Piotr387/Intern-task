package com.pp.userservice.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.userservice.response.ErrorMessages;
import com.pp.userservice.response.UserServiceException;
import com.pp.userservice.role.RoleEntity;
import com.pp.userservice.user.entity.UserEntity;
import com.pp.userservice.utils.Utilities;
import java.io.IOException;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationUtility {

  private final Utilities utilities;

  public void getRefreshToken(HttpServletRequest request, HttpServletResponse response, Function<String, UserEntity> getUserByLogin)
      throws IOException {
    String authorizationHeader = request.getHeader(AUTHORIZATION);

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        String refreshToken = authorizationHeader.substring("Bearer ".length());

        String login = utilities.getUserLoginFromToken(refreshToken);

        UserEntity userEntity = getUserByLogin.apply(login);

        String accessToken = utilities.createAccessToken(userEntity.getLogin(),
            request.getRequestURL().toString(),
            userEntity.getRoles().stream().map(RoleEntity::getName).toList());

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),
            utilities.createMapOfTokens(accessToken, refreshToken));
      } catch (Exception e) {
        utilities.tokenCreatingException(e, response);
      }
    } else {
      throw new UserServiceException(ErrorMessages.REFRESH_TOKEN_MISSING.getErrorMessage());
    }
  }
}

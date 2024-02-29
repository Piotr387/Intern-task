package com.pp.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String USER_ROLE = "ROLE_USER";
    private static final String ORGANIZER_ROLE = "ROLE_ORGANIZER";
    private static final List<String> ENDPOINT_NOT_SECURED_BY_ANY_ROLE = List.of(
            "/users/login",
            "/users/token/refresh/**",
            "/lectures/**",
            "/users/sign-up/**",
            "/users/v2/sign-up/**",
            "/h2/**"
    );

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Two line below is a way to create custom url endpoint for user login.
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(),getApplicationContext());
        customAuthenticationFilter.setFilterProcessesUrl("/users/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(ENDPOINT_NOT_SECURED_BY_ANY_ROLE.toArray(String[]::new)).permitAll(); //Unauthorized access, no need access_token
        http.authorizeRequests().antMatchers(GET, "/users/lectures").hasAnyAuthority(USER_ROLE);
        http.authorizeRequests().antMatchers(POST, "/users/sign-up-register").hasAnyAuthority(USER_ROLE);
        http.authorizeRequests().antMatchers(DELETE, "/users/cancel").hasAnyAuthority(USER_ROLE);
        http.authorizeRequests().antMatchers(PUT, "/users/update-email").hasAnyAuthority(USER_ROLE);
        http.authorizeRequests().antMatchers(GET, "/users/statistics/lectures-popularity",
                        "/users/statistics/thematic-path-popularity",
                "/users/organizer"//

                )
                .hasAnyAuthority(ORGANIZER_ROLE);
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(getApplicationContext()), UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

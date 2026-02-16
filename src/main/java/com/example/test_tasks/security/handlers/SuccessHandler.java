package com.example.test_tasks.security.handlers;


import com.example.test_tasks.entites.enums.AllowedProviders;
import com.example.test_tasks.security.services.JWTManagerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuccessHandler implements AuthenticationSuccessHandler {

    JWTManagerService jwtManager;

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        ResponseCookie[] cookies = jwtManager.getTokensCookieWrapped(
                jwtManager.generateTokenPairs(user.getName(), AllowedProviders.valueOf(user.getAttribute("provider")))
        );

        response.addHeader(HttpHeaders.SET_COOKIE,cookies[0].toString());
        response.addHeader(HttpHeaders.SET_COOKIE,cookies[1].toString());

        response.sendRedirect("/home/greetings");
    }
}

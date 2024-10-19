package com.anime.luminia.global.security;

import com.anime.luminia.domain.user.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

        CustomAuthenticationToken authenticationToken =
                new CustomAuthenticationToken(loginRequest.email(), loginRequest.password(), Collections.emptyList());

        if (loginRequest.rememberMe()) {
            request.setAttribute("remember-me", true);
        }

        log.info("HI");
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}

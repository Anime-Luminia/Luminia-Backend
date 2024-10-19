package com.anime.luminia.global.security;

import com.anime.luminia.domain.user.LuminiaUser;
import com.anime.luminia.domain.user.dto.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // User 객체 대신 DTO 사용
        LuminiaUser luminiaUser = (LuminiaUser) authentication.getPrincipal();

        // 필요한 필드만 직렬화하는 DTO 사용
        LoginResponse loginResponse = new LoginResponse(
                luminiaUser.getId(),
                luminiaUser.getEmail(),
                luminiaUser.getUsername()
        );

        // 응답 설정
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("Authentication 성공");
        log.info("응답 데이터: {}", loginResponse.toString());

        // JSON 응답 작성
        objectMapper.writeValue(response.getWriter(), loginResponse);

        log.info("Authentication 성공 후 응답 완료");
    }
}

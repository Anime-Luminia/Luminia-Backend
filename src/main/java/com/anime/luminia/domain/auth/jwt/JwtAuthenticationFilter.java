package com.anime.luminia.domain.auth.jwt;

import com.anime.luminia.global.error.exception.BusinessException;
import com.anime.luminia.global.error.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;


    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.info(request.toString());

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtTokenProvider.resolveAccessToken(request.getHeader(AUTHORIZATION_HEADER));

        log.info(token);

        try {
            if (token != null){
                if (jwtTokenProvider.validateToken(token)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
                    log.info(String.valueOf(principalDetails.getUserId()));
                    String refreshToken = redisTemplate.opsForValue().get(("refreshToken::" + principalDetails.getUserId()));
                    log.warn(refreshToken);
                    if (!(refreshToken == null || refreshToken.isBlank())) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        handleUnauthorizedException("로그아웃한 유저", response);
                        return;
                    }
                }
            }
        } catch (SecurityException | MalformedJwtException e) {
            // 401 Unauthorized
            handleUnauthorizedException("Invalid JWT Token", response);
            return;
        } catch (ExpiredJwtException e) {
            // 401 Forbidden
            handleUnauthorizedException("Expired JWT Token", response);
            return;
        } catch (UnsupportedJwtException e) {
            // 400 Bad Request
            handleBadRequestException("Unsupported JWT Token", response);
            return;
        } catch (IllegalArgumentException e) {
            // 400 Bad Request
            handleBadRequestException("JWT claims string is empty", response);
            return;
        } catch (JwtTokenInvalidException e) {
            handleUnauthorizedException("Invalid JWT Token", response);
            return;
        } catch (Exception e) {
            // 예외 처리
            handleException(e, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void handleBadRequestException(String message,HttpServletResponse response) throws IOException {
        // 400 Bad Request
        log.error(message);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        BusinessException errorResponse = new BusinessException(message, ErrorCode.HMAC_NOT_VALID);
        writeErrorResponse(response, errorResponse);
    }

    private void handleUnauthorizedException(String message, HttpServletResponse response) throws IOException {
        // 401 Unauthorized
        log.error(message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        BusinessException businessException = new BusinessException(message, ErrorCode.HMAC_NOT_VALID);
        writeErrorResponse(response, businessException);
    }

    private void handleForbiddenException(HttpServletResponse response) throws IOException {
        // 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        BusinessException businessException = new BusinessException("권한 거부", ErrorCode.HMAC_NOT_VALID);
        writeErrorResponse(response, businessException);
    }

    private void handleException(Exception e, HttpServletResponse response) throws IOException {
        // 예외에 따라 적절한 응답 생성
        log.error(e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        BusinessException businessException = new BusinessException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        writeErrorResponse(response, businessException);
    }

    private void writeErrorResponse(HttpServletResponse response, BusinessException businessException) throws IOException {
        // BusinessException을 JSON 형태로 변환하여 클라이언트에 응답 전송
        log.error(businessException.getMessage());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(businessException));
    }
}
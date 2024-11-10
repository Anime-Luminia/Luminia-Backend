package com.anime.luminia.domain.auth.service;

import com.anime.luminia.domain.auth.dto.TokenResponse;
import com.anime.luminia.domain.auth.jwt.JwtTokenProvider;
import com.anime.luminia.domain.auth.jwt.PrincipalDetails;
import com.anime.luminia.domain.user.LuminiaUser;
import com.anime.luminia.domain.user.LuminiaUserRepositroy;
import com.anime.luminia.domain.user.Role;
import com.anime.luminia.domain.user.dto.LoginRequest;
import com.anime.luminia.domain.user.dto.RegisterRequest;
import com.anime.luminia.global.error.exception.BusinessException;
import com.anime.luminia.global.error.exception.ErrorCode;
import com.anime.luminia.global.util.CookieUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.anime.luminia.global.util.CookieUtils.COOKIE_REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final LuminiaUserRepositroy userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.token.access-expiration-time}")
    private Long accessTokenExpirationTime;

    @Value("${jwt.token.refresh-expiration-time}")
    private Long refreshTokenExpirationTime;

    public TokenResponse reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        if(!StringUtils.hasText(refreshToken)) throw new BusinessException(ErrorCode.NOT_EXIST_REFRESH_TOKEN);

        jwtTokenProvider.validateToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        PrincipalDetails principalDetails;

        if (authentication.getPrincipal() instanceof PrincipalDetails) {
            principalDetails = (PrincipalDetails) authentication.getPrincipal();
        } else throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);

        String redisRefreshToken = redisTemplate.opsForValue().get(("refreshToken::" + principalDetails.getUserId()));
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(principalDetails);
        CookieUtils.addCookie(
                response,
                COOKIE_REFRESH_TOKEN,
                newRefreshToken,
                (int) TimeUnit.MILLISECONDS.toSeconds(refreshTokenExpirationTime));

        return new TokenResponse(
                jwtTokenProvider.generateAccessToken(principalDetails)
        );
    }

    @Transactional
    public LuminiaUser registerUser(RegisterRequest request){

        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new BusinessException(ErrorCode.ALREADY_EMAIL_EXIST);
        }

        LuminiaUser user = LuminiaUser
                .builder()
                .roles(Collections.singleton(Role.ROLE_USER))
                .username(request.nickName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public TokenResponse signIn(LoginRequest dto, HttpServletResponse response) {
        LuminiaUser user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        PrincipalDetails principalDetails = new PrincipalDetails(user.getId(),
                user.getEmail(), user.getUsername(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        String accessToken = jwtTokenProvider.generateAccessToken(principalDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principalDetails);

        CookieUtils.addCookie(
                response,
                COOKIE_REFRESH_TOKEN,
                refreshToken,
                (int) TimeUnit.MILLISECONDS.toSeconds(refreshTokenExpirationTime));

        return new TokenResponse(accessToken);
    }

    public void logout(String accessToken, HttpServletRequest request,
                       HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        jwtTokenProvider.validateToken(refreshToken);

        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String userId = String.valueOf(claims.getSubject());
        String key = "refreshToken::" + userId;
        log.info(key);

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.opsForValue().getAndDelete(key);
        } else {
            throw new BusinessException(ErrorCode.NOT_EXIST_REFRESH_TOKEN);
        }

        redisTemplate.opsForValue().set(accessToken, "logout", accessTokenExpirationTime,
                TimeUnit.MILLISECONDS);

        CookieUtils.deleteCookie(request, response, CookieUtils.COOKIE_REFRESH_TOKEN);
    }
}

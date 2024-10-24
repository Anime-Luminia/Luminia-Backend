package com.anime.luminia.domain.auth.jwt;

import com.anime.luminia.global.util.CookieUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String DELIMITER = ",";
    public Long AC_EXPIRATION_IN_MS;
    public Long RF_EXPIRATION_IN_MS;
    private final RedisTemplate<String, String> redisTemplate;
    private final Key jwtSecret;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String jwtSecretStr,
            RedisTemplate<String, String> redisTemplate) {
        this.jwtSecret = Keys.hmacShaKeyFor(jwtSecretStr.getBytes());
        this.redisTemplate = redisTemplate;
    }

    @Value("${jwt.token.access-expiration-time}")
    public void setAcExpirationInMs(Long time) {
        AC_EXPIRATION_IN_MS = time;
    }

    @Value("${jwt.token.refresh-expiration-time}")
    public void setRfExpirationInMs(Long time) {
        RF_EXPIRATION_IN_MS = time;
    }

    public String generateAccessToken(PrincipalDetails principalDetails) {
        String authorities = principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER));
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(String.valueOf(principalDetails.getUserId()))
                .claim(CLAIM_EMAIL, principalDetails.getEmail())
                .claim(CLAIM_NAME, principalDetails.getUsername())
                .claim(CLAIM_AUTHORITIES, authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + AC_EXPIRATION_IN_MS))
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(PrincipalDetails principalDetails) {
        String authorities = principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER));
        long now = System.currentTimeMillis();

        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(principalDetails.getUserId()))
                .claim(CLAIM_EMAIL, principalDetails.getEmail())
                .claim(CLAIM_NAME, principalDetails.getUsername())
                .claim(CLAIM_AUTHORITIES, authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + RF_EXPIRATION_IN_MS))
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();

        redisTemplate.opsForValue().set(
                String.valueOf("refreshToken::" + principalDetails.getUserId()),
                refreshToken,
                RF_EXPIRATION_IN_MS,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("걸렸당" + e.getMessage());
            throw JwtTokenInvalidException.INSTANCE;
        }
    }

    /**
     * DB 조회가 이뤄진다면 JWT 토큰의 장점을 얻어가지 못한다.
     *
     * @link https://velog.io/@tlatldms/%EC%84%9C%EB%B2%84%EA%B0%9C%EB%B0%9C%EC%BA%A0%ED%94%84-Spring-security-refreshing-JWT-DB%EC%A0%91%EA%B7%BC%EC%97%86%EC%9D%B4-%EC%9D%B8%EC%A6%9D%EA%B3%BC-%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = this.parseClaims(accessToken);

        long userId = Long.parseLong(claims.getSubject());
        String email = claims.get(CLAIM_EMAIL, String.class);
        String userName = claims.get(CLAIM_NAME, String.class);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(CLAIM_AUTHORITIES).toString().split(DELIMITER))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        PrincipalDetails principalDetails = new PrincipalDetails(userId, email, userName, authorities);
        return new UsernamePasswordAuthenticationToken(principalDetails, null, authorities);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveAccessToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        Optional<Cookie> cookie = CookieUtils.getCookie(request, CookieUtils.COOKIE_REFRESH_TOKEN);
        return cookie.map(Cookie::getValue).orElse("");
    }
}


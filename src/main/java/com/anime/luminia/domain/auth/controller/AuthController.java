package com.anime.luminia.domain.auth.controller;

import com.anime.luminia.domain.auth.dto.TokenResponse;
import com.anime.luminia.domain.auth.service.AuthService;
import com.anime.luminia.domain.user.LuminiaUser;
import com.anime.luminia.domain.user.dto.LoginRequest;
import com.anime.luminia.domain.user.dto.RegisterRequest;
import com.anime.luminia.global.dto.ApiResult;
import com.anime.luminia.global.util.Timer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResult<TokenResponse>> reissue(HttpServletRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.reissueToken(request, response);
        return ResponseEntity
                .ok(ApiResult.success("Successfully get token", tokenResponse));
    }

    @Timer
    @PostMapping("/login")
    public ResponseEntity<ApiResult<TokenResponse>> signin(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        log.debug("SigninRequest: {}", loginRequest);
        TokenResponse tokenResponse = authService.signIn(loginRequest, response);
        return ResponseEntity
                .ok(ApiResult.success("Log in Successful", tokenResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResult<LuminiaUser>> register(@RequestBody @Valid RegisterRequest request) {
        LuminiaUser user = authService.registerUser(request);
        return ResponseEntity.ok(ApiResult.success("Register Successful", user));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResult<String>> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
                                                    HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            accessToken =  accessToken.substring(7);
        }
        authService.logout(accessToken, request, response);

        return ResponseEntity
                .ok(ApiResult.success("Successfully logout token", "Logout successful"));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResult<Boolean>> checkAuthStatus(@AuthenticationPrincipal LuminiaUser user) {
        if(user == null) return ResponseEntity.ok(ApiResult.success("Non Authenticated", false));
        else return ResponseEntity.ok(ApiResult.success("Authenticated", true));
    }
}

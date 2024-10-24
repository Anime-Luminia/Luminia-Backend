package com.anime.luminia.domain.user;

import com.anime.luminia.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResult<LuminiaUser>> getMyProfile(@AuthenticationPrincipal LuminiaUser user) {
        return ResponseEntity.ok(ApiResult.success("Successfully get Info",userService.getUserInfo(user.getId())));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/status")
    public ResponseEntity<ApiResult<Boolean>> checkAuthStatus(@AuthenticationPrincipal LuminiaUser user) {
        if(user == null) return ResponseEntity.ok(ApiResult.success("Non Authenticated", false));
        else return ResponseEntity.ok(ApiResult.success("Authenticated", true));
    }

    @GetMapping("/hello")
    public ResponseEntity<ApiResult<String>> hello() {
        return ResponseEntity.ok(ApiResult.success("Hello"));
    }
}

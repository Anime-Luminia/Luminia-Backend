package com.anime.luminia.domain.user;

import com.anime.luminia.domain.user.dto.RegisterRequest;
import com.anime.luminia.global.dto.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResult<LuminiaUser>> register(@RequestBody @Valid RegisterRequest request) {
        System.out.println(request.nickName() + " " + request.password() + " " + request.email());
        LuminiaUser user = userService.registerUser(request);

        return ResponseEntity.ok(ApiResult.success("Register Successful", user));
    }
}

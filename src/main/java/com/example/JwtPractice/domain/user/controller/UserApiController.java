package com.example.JwtPractice.domain.user.controller;

import com.example.JwtPractice.domain.token.model.TokenResponse;
import com.example.JwtPractice.domain.user.model.LoginRequest;
import com.example.JwtPractice.domain.user.model.RegisterRequest;
import com.example.JwtPractice.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest){
        return userService.register(registerRequest);
    }

    // 로그인
    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }


    // me
}

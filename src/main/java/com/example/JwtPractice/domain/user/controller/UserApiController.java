package com.example.JwtPractice.domain.user.controller;

import com.example.JwtPractice.domain.token.model.TokenResponse;
import com.example.JwtPractice.domain.user.service.UserService;
import com.example.JwtPractice.domain.user.model.UserLoginRequest;
import com.example.JwtPractice.domain.user.model.UserRegisterRequest;
import com.example.JwtPractice.domain.user.model.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserApiController {
    private final UserService userService;
    @GetMapping("/register")
    public String register(){
        userService.register();
        return "성공";
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody UserLoginRequest request){
        return userService.login(request.getName(), request.getPassword());
    }

    @GetMapping("/me")
    public UserRegisterResponse me(){
        return userService.me();
    }
}

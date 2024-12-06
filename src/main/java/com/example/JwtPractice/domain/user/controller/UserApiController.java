package com.example.JwtPractice.domain.user.controller;

import com.example.JwtPractice.domain.user.db.UserRepository;
import com.example.JwtPractice.domain.user.model.UserLoginRequest;
import com.example.JwtPractice.domain.user.model.UserRegisterRequest;
import com.example.JwtPractice.domain.user.model.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserApiController {
    private final UserRepository userRepository;
    @PostMapping("/register")
    public UserRegisterResponse register(@RequestBody UserRegisterRequest request){
        return userRepository.register(request);
    }

    @PostMapping("/login")
    public Long login(@RequestBody UserLoginRequest request){
        return userRepository.login(request.getName(), request.getPassword());
    }

    @GetMapping("/me/{id}")
    public UserRegisterResponse me(@PathVariable Long id){
        return userRepository.me(id);
    }
}

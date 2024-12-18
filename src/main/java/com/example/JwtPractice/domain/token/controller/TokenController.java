package com.example.JwtPractice.domain.token.controller;

import com.example.JwtPractice.domain.token.model.TokenDto;
import com.example.JwtPractice.domain.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;


    // Post로 하는 것이 일반적
    @GetMapping("/refresh/{token}")
    public TokenDto reissueRefreshToken(@PathVariable String token){
        return tokenService.reissueRefreshToken(token);
    }
}

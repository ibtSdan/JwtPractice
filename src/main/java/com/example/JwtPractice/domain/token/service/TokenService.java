package com.example.JwtPractice.domain.token.service;

import com.example.JwtPractice.domain.token.helper.TokenHelper;
import com.example.JwtPractice.domain.token.model.TokenDto;
import com.example.JwtPractice.domain.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenHelper tokenHelper;

    public TokenDto issueAccessToken(UserEntity user){
        var map = new HashMap<String, Object>();
        map.put("userId",user.getId());
        return tokenHelper.issueAccessToken(map);
    }

    public TokenDto issueRefreshToken(UserEntity user){
        var map = new HashMap<String, Object>();
        map.put("userId",user.getId());
        return tokenHelper.issueRefreshToken(map);
    }

    public Long validationToken(String token){
        var map = tokenHelper.validationToken(token);
        return Long.parseLong(map.get("userId").toString());
    }
}

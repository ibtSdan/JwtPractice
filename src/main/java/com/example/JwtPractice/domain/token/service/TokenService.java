package com.example.JwtPractice.domain.token.service;

import com.example.JwtPractice.domain.token.helper.TokenHelper;
import com.example.JwtPractice.domain.token.model.TokenDto;
import com.example.JwtPractice.domain.token.repository.TokenRepository;
import com.example.JwtPractice.domain.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenHelper tokenHelper;
    private final TokenRepository tokenRepository;

    public TokenDto issueAccessToken(Long userId){
        var map = new HashMap<String, Object>();
        map.put("userId",userId);
        return tokenHelper.issueAccessToken(map);
    }

    public TokenDto issueRefreshToken(Long userId){
        var map = new HashMap<String, Object>();
        map.put("userId",userId);
        return tokenHelper.issueRefreshToken(map);
    }

    public Long validationToken(String token){
        var map = tokenHelper.validationToken(token);
        return Long.parseLong(map.get("userId").toString());
    }

    public TokenDto reissueRefreshToken(String token) {
        tokenHelper.validationToken(token);
        // 유효한 refreshToken
        // accessToken 재발급
        var entity = tokenRepository.findByRefreshToken(token).get();
        var userId = entity.getUserId();
        return issueAccessToken(userId);

    }
}

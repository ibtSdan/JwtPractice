package com.example.JwtPractice.domain.token.service;

import com.example.JwtPractice.domain.token.helper.TokenHelper;
import com.example.JwtPractice.domain.token.model.TokenDto;
import com.example.JwtPractice.domain.token.repository.TokenRepository;
import com.example.JwtPractice.domain.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenHelper tokenHelper;
    private final TokenRepository tokenRepository;

    public TokenDto issueAccessToken(Long userId, Collection<? extends GrantedAuthority> authorities){
        var map = new HashMap<String, Object>();
        map.put("userId",userId);
        var authoritiesList = authorities.stream()
                .map(GrantedAuthority::getAuthority).toList();
        map.put("authorities", authoritiesList);
        return tokenHelper.issueAccessToken(map);
    }

    public TokenDto issueRefreshToken(Long userId, Collection<? extends GrantedAuthority> authorities){
        var map = new HashMap<String, Object>();
        map.put("userId",userId);
        var authoritiesList = authorities.stream()
                .map(GrantedAuthority::getAuthority).toList();
        map.put("authorities", authoritiesList);
        return tokenHelper.issueRefreshToken(map);
    }

    public Map<String, Object> validationToken(String token){
        return tokenHelper.validationToken(token);
    }

    public TokenDto reissueRefreshToken(String token) {
        tokenHelper.validationToken(token);
        // 유효한 refreshToken
        // accessToken 재발급
        var entity = tokenRepository.findByRefreshToken(token).get();
        var userId = entity.getUserId();
        var roles = entity.getRole();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String role:roles.split(",")){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return issueAccessToken(userId,authorities);

    }
}

package com.example.JwtPractice.domain.token.helper;

import com.example.JwtPractice.domain.token.model.TokenDto;
import com.example.JwtPractice.domain.token.repository.TokenEntity;
import com.example.JwtPractice.domain.token.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenHelper {
    @Value("${token.secret.key}")
    private String secretKey;
    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;
    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    private final TokenRepository tokenRepository;
    public TokenDto issueAccessToken(Map<String, Object> data){
        // 키 만들기
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        // 만료시간 설정
        var expiredAt = LocalDateTime.now().plusHours(accessTokenPlusHour);
        var expiredAtInstance = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        // 토큰 생성
        var jwtToken = Jwts.builder()
                .signWith(key)
                .claims(data)
                .expiration(expiredAtInstance)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredAt)
                .build();
    }

    public TokenDto issueRefreshToken(Map<String, Object> data){
        var expiredAt = LocalDateTime.now().plusHours(refreshTokenPlusHour);
        var expiredAtInstance = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var jwtToken = Jwts.builder()
                .signWith(key)
                .expiration(expiredAtInstance)
                .compact();

        var userId = Long.parseLong(data.get("userId").toString());
        var authorities = data.get("authorities").toString();
        authorities = authorities.substring(1,authorities.length()-1);

        if(tokenRepository.existsByUserId(userId)){
            var entity = tokenRepository.findByUserId(userId).get();
            entity.setRefreshToken(jwtToken);
            tokenRepository.save(entity);
        } else{
            var entity = TokenEntity.builder()
                    .refreshToken(jwtToken)
                    .userId(userId)
                    .role(authorities)
                    .build();
            tokenRepository.save(entity);
        }

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredAt)
                .build();
    }

    public Map<String, Object> validationToken(String token){
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        var parser = Jwts.parser()
                .setSigningKey(key)
                .build();

        try{
            var result = parser.parseClaimsJws(token);
            return new HashMap<String, Object>(result.getPayload());
        } catch (Exception e){
            if(e instanceof SignatureException){
                throw new RuntimeException("유효하지 않은 토큰");
            } else if(e instanceof ExpiredJwtException){
                throw new RuntimeException("만료된 토큰");
            } else {
                throw new RuntimeException("토큰 에러");
            }
        }
    }
}

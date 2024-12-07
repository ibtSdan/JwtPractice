package com.example.JwtPractice.domain.token.helper;

import com.example.JwtPractice.domain.token.model.TokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class JwtTokenHelper {
    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    public TokenDto issueAccessToken(Map<String, Object> data){
        // 만료 시간
        var expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);
        // 객체
        var expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var jwtToken = Jwts.builder()
                .signWith(key)
                .claims(data)
                .expiration(expiredAt)
                .compact();
        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    public TokenDto issueRefreshToken(Map<String, Object> data){
        var expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);
        var expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        var jwtToken = Jwts.builder()
                .signWith(key)
                .claims(data)
                .expiration(expiredAt)
                .compact();
        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
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
                // 토큰이 유효하지 않을 때
                throw new RuntimeException("유효하지 않은 토큰");
            }
            else if(e instanceof ExpiredJwtException){
                // 만료된 토큰
                throw new RuntimeException("만료된 토큰");
            }
            else{
                // 그 외 에러
                throw new RuntimeException("그 외 토큰 에러");
            }
        }

    }
}

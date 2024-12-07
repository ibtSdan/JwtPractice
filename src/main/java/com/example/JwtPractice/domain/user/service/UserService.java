package com.example.JwtPractice.domain.user.service;

import com.example.JwtPractice.domain.token.helper.JwtTokenHelper;
import com.example.JwtPractice.domain.token.model.TokenResponse;
import com.example.JwtPractice.domain.user.db.UserEntity;
import com.example.JwtPractice.domain.user.model.UserRegisterRequest;
import com.example.JwtPractice.domain.user.model.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private List<UserEntity> list = new ArrayList<>();

    private final JwtTokenHelper jwtTokenHelper;


    public void register(){
        list.add(new UserEntity(1L,"홍길동","1234","hong@gmail.com",LocalDateTime.now()));
        list.add(new UserEntity(2L, "김김","1234","kim@gmail.com",LocalDateTime.now()));
    }

    public TokenResponse login(String name, String password) {
        UserEntity userEntity = null;
        for(UserEntity entity : list){
            if((entity.getName().equals(name))&&(entity.getPassword().equals(password))){
                userEntity = entity;
            }
        }
        if(userEntity == null){
            throw new RuntimeException("name, password 다름");
        }
        var data = new HashMap<String, Object>();
        data.put("userId",userEntity.getId());
        var accessToken = jwtTokenHelper.issueAccessToken(data);
        var refreshToken = jwtTokenHelper.issueRefreshToken(data);

        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();
    }

    public UserRegisterResponse me() {
        var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        var userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        for(UserEntity userEntity : list){
            if(userEntity.getId().equals(userId)){
                return UserRegisterResponse.builder()
                        .name(userEntity.getName())
                        .password(userEntity.getPassword())
                        .email(userEntity.getEmail())
                        .registerAt(userEntity.getRegisterAt())
                        .build();
            }
        }
        return null;
    }
}

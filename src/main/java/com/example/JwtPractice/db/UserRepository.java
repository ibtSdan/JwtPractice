package com.example.JwtPractice.db;

import com.example.JwtPractice.domain.user.model.UserRegisterRequest;
import com.example.JwtPractice.domain.user.model.UserRegisterResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class UserRepository {
    private List<UserEntity> list = new ArrayList<>();

    public UserRegisterResponse register(UserRegisterRequest request){
        var entity = UserEntity.builder()
                .name(request.getName())
                .password(request.getPassword())
                .email(request.getEmail())
                .registerAt(LocalDateTime.now())
                .build();
        list.add(entity);
        return UserRegisterResponse.builder()
                .name(entity.getName())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .registerAt(entity.getRegisterAt())
                .build();
    }
}

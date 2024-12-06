package com.example.JwtPractice.domain.user.db;

import com.example.JwtPractice.domain.user.model.UserRegisterRequest;
import com.example.JwtPractice.domain.user.model.UserRegisterResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class UserRepository {
    private List<UserEntity> list = new ArrayList<>();
    private Long id = 0L;


    public UserRegisterResponse register(UserRegisterRequest request){
        var entity = UserEntity.builder()
                .id(id)
                .name(request.getName())
                .password(request.getPassword())
                .email(request.getEmail())
                .registerAt(LocalDateTime.now())
                .build();
        list.add(entity);
        id++;
        return UserRegisterResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .registerAt(entity.getRegisterAt())
                .build();
    }

    public Long login(String name, String password) {
        var entity = list.stream().findFirst().map(it -> {
            if((it.getName().equals(name))&&it.getPassword().equals(password)) {
                return it;
            } else{
            throw new RuntimeException("유저 없음");}
        });
        return entity.get().getId();
    }

    public UserRegisterResponse me(Long id) {
        for (UserEntity entity : list) {
            if (entity.getId() == id) {
                return UserRegisterResponse.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .password(entity.getPassword())
                        .email(entity.getEmail())
                        .registerAt(entity.getRegisterAt())
                        .build();
            }
        }
        return null;
    }
}

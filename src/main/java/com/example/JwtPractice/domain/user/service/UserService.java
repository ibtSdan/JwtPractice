package com.example.JwtPractice.domain.user.service;

import com.example.JwtPractice.domain.token.model.TokenDto;
import com.example.JwtPractice.domain.token.model.TokenResponse;
import com.example.JwtPractice.domain.token.service.TokenService;
import com.example.JwtPractice.domain.user.model.LoginRequest;
import com.example.JwtPractice.domain.user.model.RegisterRequest;
import com.example.JwtPractice.domain.user.repository.UserEntity;
import com.example.JwtPractice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public String register(RegisterRequest registerRequest) {
        var entity = UserEntity.builder()
                .name(registerRequest.getName())
                .password(registerRequest.getPassword())
                .status("REGISTERED")
                .registeredAt(LocalDateTime.now())
                .build();
        userRepository.save(entity);
        return "등록완료";

    }



    public TokenResponse login(LoginRequest loginRequest){
        String name = loginRequest.getName();
        String password = loginRequest.getPassword();
        var entity = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("해당하는 사용자가 존재하지 않습니다."));
        if(!(entity.getPassword().equals(password))){
            throw new RuntimeException("비밀번호가 다릅니다.");
        }
        var accessToken = tokenService.issueAccessToken(entity);
        var refreshToken = tokenService.issueRefreshToken(entity);

        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();

    }


}

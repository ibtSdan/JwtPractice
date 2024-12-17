package com.example.JwtPractice.domain.user.service;

import com.example.JwtPractice.domain.token.model.TokenDto;
import com.example.JwtPractice.domain.token.model.TokenResponse;
import com.example.JwtPractice.domain.token.service.TokenService;
import com.example.JwtPractice.domain.user.model.CustomUserDetails;
import com.example.JwtPractice.domain.user.model.LoginRequest;
import com.example.JwtPractice.domain.user.model.MeResponse;
import com.example.JwtPractice.domain.user.model.RegisterRequest;
import com.example.JwtPractice.domain.user.repository.UserEntity;
import com.example.JwtPractice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) {
        // 암호화
        var encodePw = passwordEncoder.encode(registerRequest.getPassword());

        var entity = UserEntity.builder()
                .name(registerRequest.getName())
                .password(encodePw)
                .status("REGISTERED")
                .registeredAt(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
        userRepository.save(entity);
        return "등록완료";
    }



    public TokenResponse login(LoginRequest loginRequest){
        // 인증에 성공하면 자동으로 user 정보 갱신해줌. 그래서 UsernamePasswordAuthenticationToken 사용
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getName(), loginRequest.getPassword()
        );

        // authenticationManager 가 호출되면, 자동으로 authenticationProvider 호출
        // Provider 에서 설정된 UserDetailsService로 해당하는 user를 db에서 가져온다
        // 가져온 user를 match로 검증
        // 성공하면 authentication 객체 반환
        var authentication = authenticationManager.authenticate(authenticationToken);

        var user = (CustomUserDetails) authentication.getPrincipal();
        var userId = Long.parseLong(user.getUserId().toString());
        var authorities = user.getAuthorities();

        var accessToken = tokenService.issueAccessToken(userId, authorities);
        var refreshToken = tokenService.issueRefreshToken(userId, authorities);

        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();

    }

    public String me() {
        var requestContext = SecurityContextHolder.getContext().getAuthentication();
        var userId = Long.parseLong(requestContext.getPrincipal().toString());
        return "me: "+userId;
    }

    public String admin() {
        var requestContext = SecurityContextHolder.getContext().getAuthentication();
        var userId = Long.parseLong(requestContext.getPrincipal().toString());
        return "어드민: "+userId;
    }
}

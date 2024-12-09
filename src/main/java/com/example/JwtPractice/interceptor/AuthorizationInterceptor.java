package com.example.JwtPractice.interceptor;

import com.example.JwtPractice.domain.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var accessToken = request.getHeader("authorization-token");
        var userId = tokenService.validationToken(accessToken);

        var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        requestContext.setAttribute("userId",userId, RequestAttributes.SCOPE_REQUEST);
        return true;
    }
}

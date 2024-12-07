package com.example.JwtPractice.interceptor;

import com.example.JwtPractice.domain.token.helper.JwtTokenHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final JwtTokenHelper jwtTokenHelper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var accessToken = request.getHeader("authorization-token");
        var map = jwtTokenHelper.validationToken(accessToken);

        var userId = Long.parseLong(map.get("userId").toString());
        var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        requestContext.setAttribute("userId",userId, RequestAttributes.SCOPE_REQUEST);
        return true;
    }
}

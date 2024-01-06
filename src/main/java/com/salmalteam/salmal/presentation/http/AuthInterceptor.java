package com.salmalteam.salmal.presentation.http;

import com.salmalteam.salmal.domain.auth.application.TokenExtractor;
import com.salmalteam.salmal.domain.auth.application.TokenProvider;
import com.salmalteam.salmal.domain.auth.entity.TokenRepository;
import com.salmalteam.salmal.domain.auth.exception.AuthException;
import com.salmalteam.salmal.domain.auth.exception.AuthExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final TokenExtractor tokenExtractor;
    private final TokenRepository tokenRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // @Login 어노테이션이 존재하지 않는 경우 : 모두 허용
        if(!(handler instanceof HandlerMethod) || getLoginAnnotation(handler) == null){
            return true;
        }

        // 인증 헤더를 가지는 경우 : 헤더가 유효한 경우 허용
        if(hasAuthorization(request)){
            final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final String accessToken = tokenExtractor.extractAccessTokenFromHeader(authorizationHeader);
            validateNotLoggedOutAccessToken(accessToken);
            validateAccessToken(accessToken);
            return true;
        }

        // Login 어노테이션이 존재하는데 인증 헤더를 가지지 않는 경우
        validateTokenRequired(handler);
        return true;
    }

    private void validateNotLoggedOutAccessToken(final String accessToken) {
        if(tokenRepository.existsLogoutAccessTokenById(accessToken)){
            throw new AuthException(AuthExceptionType.LOGGED_OUT_ACCESS_TOKEN);
        }
    }


    private boolean hasAuthorization(HttpServletRequest request){
        return request.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }

    private void validateAccessToken(final String accessToken){

        if(!tokenProvider.isValidAccessToken(accessToken)){
            throw new AuthException(AuthExceptionType.NOT_VALID_ACCESS_TOKEN);
        }
    }

    private void validateTokenRequired(final Object handler) {
        final Login loginAnnotation = getLoginAnnotation(handler);
        if(loginAnnotation != null){
            throw new AuthException(AuthExceptionType.UNAUTHORIZED_NO_ACCESS_TOKEN);
        }
    }

    private Login getLoginAnnotation(final Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethodAnnotation(Login.class);
    }

}

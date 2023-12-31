package com.salmalteam.salmal.presentation;

import com.salmalteam.salmal.application.auth.TokenExtractor;
import com.salmalteam.salmal.application.auth.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;
    private final TokenExtractor tokenExtractor;
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader == null) return null;
        final String accessToken = tokenExtractor.extractAccessTokenFromHeader(authorizationHeader);
        return tokenProvider.getPayLoad(accessToken);
    }
}

package com.salmalteam.salmal.presentation.http.auth;

import static com.salmalteam.salmal.auth.exception.AuthExceptionType.*;
import static org.springframework.http.HttpHeaders.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.salmalteam.salmal.auth.application.AuthPayloadGenerator;
import com.salmalteam.salmal.auth.application.TokenExtractor;
import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.auth.exception.AuthException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

	private final TokenExtractor tokenExtractor;
	private final AuthPayloadGenerator authPayloadGenerator;
	private final AuthenticationContext authenticationContext;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String token = tokenExtractor.extractByHeader(request.getHeader(AUTHORIZATION))
			.orElseThrow(() -> new AuthException(UNAUTHORIZED_NO_ACCESS_TOKEN));

		MemberPayLoad memberPayLoad = authPayloadGenerator.generateByToken(token);
		authenticationContext.setAuthContext(memberPayLoad.getId(), memberPayLoad.getRole());
		return true;
	}
}

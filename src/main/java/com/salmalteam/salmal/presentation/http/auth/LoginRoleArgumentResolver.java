package com.salmalteam.salmal.presentation.http.auth;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.salmalteam.salmal.auth.entity.Role;
import com.salmalteam.salmal.auth.exception.AuthException;
import com.salmalteam.salmal.auth.exception.AuthExceptionType;

public class LoginRoleArgumentResolver implements HandlerMethodArgumentResolver {

	private final AuthenticationContext authenticationContext;
	private final Role role;

	public LoginRoleArgumentResolver(AuthenticationContext authenticationContext, Role role) {
		Assert.notNull(authenticationContext, "The authenticationContext must not be null!");
		Assert.notNull(role, "The Role must not be null!");
		this.authenticationContext = authenticationContext;
		this.role = role;
	}

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return parameter.hasParameterAnnotation(role.getAnnotation());
	}

	@Override
	public Long resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
		final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
		if (!role.equals(authenticationContext.getRole())) {
			throw new AuthException(AuthExceptionType.PERMISSION_DOSE_NOT_EXIST);
		}
		return authenticationContext.getId();
	}
}

package com.salmalteam.salmal.presentation.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.salmalteam.salmal.auth.entity.Role;
import com.salmalteam.salmal.presentation.http.auth.AuthInterceptor;
import com.salmalteam.salmal.presentation.http.auth.AuthenticationContext;
import com.salmalteam.salmal.presentation.http.auth.LoginRoleArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private static final String LOGIN_URL = "/api/auth/login";
	private static final String SIGN_UP_URL = "/api/auth/signup/**";
	private static final String HEALTH_CHECK_URL = "/api/health";

	private final AuthInterceptor authInterceptor;
	private final AuthenticationContext authenticationContext;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
			.addPathPatterns("/api/**")
			.excludePathPatterns(LOGIN_URL, SIGN_UP_URL, HEALTH_CHECK_URL);
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginRoleArgumentResolver());
	}

	@Bean
	public LoginRoleArgumentResolver loginRoleArgumentResolver() {
		return new LoginRoleArgumentResolver(authenticationContext, Role.MEMBER);
	}
}

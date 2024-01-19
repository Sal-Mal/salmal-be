package com.salmalteam.salmal.presentation.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.salmalteam.salmal.presentation.http.auth.LoginRoleArgumentResolver;
import com.salmalteam.salmal.presentation.http.auth.AuthInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	private final LoginRoleArgumentResolver authArgumentResolver;

	private final AuthInterceptor authInterceptor;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
			.excludePathPatterns("/api/auth/login", "api/auth/signup/**", "/api/auth/reissue");
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authArgumentResolver);
	}
}

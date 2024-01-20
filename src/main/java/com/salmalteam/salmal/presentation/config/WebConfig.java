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

	private final AuthenticationContext authenticationContext;

	private final AuthInterceptor authInterceptor;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
			.excludePathPatterns("/api/auth/login", "api/auth/signup/**");
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

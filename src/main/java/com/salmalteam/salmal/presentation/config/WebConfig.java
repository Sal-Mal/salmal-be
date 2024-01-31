package com.salmalteam.salmal.presentation.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.salmalteam.salmal.auth.application.AuthPayloadGenerator;
import com.salmalteam.salmal.auth.application.TokenExtractor;
import com.salmalteam.salmal.auth.entity.Role;
import com.salmalteam.salmal.presentation.http.auth.AuthInterceptor;
import com.salmalteam.salmal.presentation.http.auth.AuthenticationContext;
import com.salmalteam.salmal.presentation.http.auth.LoginRoleArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final TokenExtractor tokenExtractor;
	private final AuthPayloadGenerator authPayloadGenerator;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor())
			.excludePathPatterns("/api/auth/login", "api/auth/signup/**");
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginRoleArgumentResolver());
	}

	@Bean
	public LoginRoleArgumentResolver loginRoleArgumentResolver() {
		return new LoginRoleArgumentResolver(authenticationContext(), Role.MEMBER);
	}

	@Bean
	public AuthenticationContext authenticationContext() {
		return new AuthenticationContext();
	}

	@Bean
	public AuthInterceptor authInterceptor() {
		return new AuthInterceptor(tokenExtractor, authPayloadGenerator, authenticationContext());
	}
}

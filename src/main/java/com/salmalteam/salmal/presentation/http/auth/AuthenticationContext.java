package com.salmalteam.salmal.presentation.http.auth;

import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

import com.salmalteam.salmal.auth.entity.Role;

@Scope("request")
public class AuthenticationContext {

	private Long id;
	private Role role = Role.ANONYMOUS;

	public AuthenticationContext() {
	}

	public void setAuthContext(Long id, Role role) {
		Assert.notNull(id, "The Id must not be null!");
		Assert.notNull(role, "The Role must not be null!");
		this.id = id;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public Role getRole() {
		return role;
	}
}

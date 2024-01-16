package com.salmalteam.salmal.presentation.http.auth;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.salmalteam.salmal.auth.entity.Role;

@Component
@Scope("request")
public class AuthenticationContext {

	private Long id;
	private Role role = Role.ANONYMOUS;

	public AuthenticationContext() {
	}

	public void setAuthContext(Long id, Role role) {
		Assert.notNull(id, "id must be not null!");
		Assert.notNull(role, "role must be not null!");
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

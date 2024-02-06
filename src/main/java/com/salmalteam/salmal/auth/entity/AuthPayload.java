package com.salmalteam.salmal.auth.entity;

import lombok.Getter;

@Getter
public class AuthPayload {

	private final Long id;
	private final Role role;

	private AuthPayload(final Long id) {
		this.id = id;
		role = Role.ANONYMOUS;
	}

	private AuthPayload(final Long id, Role role) {
		this.id = id;
		this.role = role;
	}

	public static AuthPayload from(final Long id) {
		return new AuthPayload(id);
	}

	public static AuthPayload of(final Long id, final Role role) {
		return new AuthPayload(id, role);
	}
}

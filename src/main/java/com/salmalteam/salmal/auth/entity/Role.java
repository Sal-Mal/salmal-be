package com.salmalteam.salmal.auth.entity;

public enum Role {
	MEMBER,
	ADMIN,
	ANONYMOUS;

	public static Role of(String role) {
		return Role.of(role);
	}
}

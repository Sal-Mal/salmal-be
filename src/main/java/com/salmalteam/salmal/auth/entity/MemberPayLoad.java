package com.salmalteam.salmal.auth.entity;

import lombok.Getter;

@Getter
public class MemberPayLoad {

	private final Long id;
	private final Role role;

	private MemberPayLoad(final Long id) {
		this.id = id;
		role = Role.ANONYMOUS;
	}

	private MemberPayLoad(final Long id, Role role) {
		this.id = id;
		this.role = role;
	}

	public static MemberPayLoad from(final Long id) {
		return new MemberPayLoad(id);
	}

	public static MemberPayLoad of(final Long id, final Role role) {
		return new MemberPayLoad(id, role);
	}
}

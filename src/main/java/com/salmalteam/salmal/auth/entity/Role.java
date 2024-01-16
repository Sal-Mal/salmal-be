package com.salmalteam.salmal.auth.entity;

import java.lang.annotation.Annotation;

import com.salmalteam.salmal.auth.annotation.Anonymous;
import com.salmalteam.salmal.auth.annotation.LoginAdmin;
import com.salmalteam.salmal.auth.annotation.LoginMember;

import lombok.Getter;

@Getter
public enum Role {
	MEMBER(LoginMember.class, "일반회원"),
	ADMIN(LoginAdmin.class, "어드민"),
	ANONYMOUS(Anonymous.class, "비회원");

	private final Class<? extends Annotation> annotation;
	private final String name;

	Role(Class<? extends Annotation> annotation, String name) {
		this.annotation = annotation;
		this.name = name;
	}

	public static Role of(String role) {
		try {
			return valueOf(role);
		} catch (NullPointerException | IllegalArgumentException e) {
			throw new IllegalArgumentException("일치하는 룰이 존재하지 않습니다.");
		}
	}

}

package com.salmalteam.salmal.auth.application;

import org.springframework.context.annotation.Scope;

@Scope("request")
public class AuthenticationContext {

	private String memberId;

	public AuthenticationContext(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return memberId;
	}
}

package com.salmalteam.salmal.dto.response.auth;

public class TokenAvailableResponse {
	private final boolean available;

	public TokenAvailableResponse(boolean available) {
		this.available = available;
	}

	public boolean getAvailable() {
		return available;
	}
}

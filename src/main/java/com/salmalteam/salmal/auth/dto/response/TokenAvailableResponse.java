package com.salmalteam.salmal.auth.dto.response;

public class TokenAvailableResponse {
	private final boolean available;

	public TokenAvailableResponse(boolean available) {
		this.available = available;
	}

	public boolean getAvailable() {
		return available;
	}
}

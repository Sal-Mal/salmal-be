package com.salmalteam.salmal.support;

import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenValidator {

	private final JwtParser jwtParser;

	public TokenValidator(String secretKey) {
		jwtParser = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
			.build();
	}

	public boolean isValidate(String token) {
		jwtParser.parseClaimsJws(token);
		return true;
	}

	public <T> boolean hasClaims(String token, String key, Object value, Class<T> clazz) {
		Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
		Claims body = claimsJws.getBody();
		return body.get(key, clazz).equals(value);
	}

	public boolean hasSubject(String token, String subject) {
		Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
		Claims body = claimsJws.getBody();
		return body.getSubject().equals(subject);
	}
}

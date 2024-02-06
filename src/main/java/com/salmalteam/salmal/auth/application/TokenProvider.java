package com.salmalteam.salmal.auth.application;

import java.util.Map;

public interface TokenProvider {
	String provide(Map<String, Object> payload);
}

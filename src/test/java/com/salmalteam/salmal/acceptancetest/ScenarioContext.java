package com.salmalteam.salmal.acceptancetest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.cucumber.spring.ScenarioScope;

@ScenarioScope
@Component
public class ScenarioContext {
	private Map<String, String> context = new HashMap<>();

	public String getValue(String key) {
		assert key != null;
		return context.get(key);
	}

	public void put(String key, String value) {
		assert key != null;
		assert value != null;
		context.put(key, value);
	}
}

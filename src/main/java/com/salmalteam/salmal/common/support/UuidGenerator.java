package com.salmalteam.salmal.common.support;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UuidGenerator {
	public UUID generate() {
		return UUID.randomUUID();
	}
}

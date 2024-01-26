package com.salmalteam.salmal.notification.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageSpec {
	private String title;
	private String body;
	private Map<String, String> data;
}

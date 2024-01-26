package com.salmalteam.salmal.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Type {
	REPLY("대댓글 알림");

	private final String description;
}

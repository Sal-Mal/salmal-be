package com.salmalteam.salmal.notification.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salmalteam.salmal.notification.entity.Notification;
import com.salmalteam.salmal.notification.entity.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationDto {
	private final String uuid;
	private final Long markId;
	private final Type type;
	private final String message;
	private final boolean isRead;
	@JsonFormat(pattern = "yy-MM-dd'T'HH:mm")
	private final LocalDateTime createAt;
	private final String memberImageUrl;
	private final String imageUrl;

	public static NotificationDto create(Notification notification) {
		return new NotificationDto(notification.getUuid(), notification.getMarkId(), notification.getType(),
			notification.getMessage(), notification.isRead(), notification.getCreatedAt(), notification.getMemberImageUrl(), notification.getMarkContentImageUrl());
	}
}

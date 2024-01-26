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
	@JsonFormat(pattern = "yy-MM-ddTHH:mm")
	private final LocalDateTime createAt;

	public static NotificationDto create(Notification notification) {
		return new NotificationDto(notification.getUuid(), notification.getMarkId(), notification.getType(),
			notification.getMessage(), notification.getCreatedAt());
	}
}

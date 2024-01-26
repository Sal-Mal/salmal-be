package com.salmalteam.salmal.notification.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindNotificationResponse {
	private final List<NotificationDto> notifications;
}

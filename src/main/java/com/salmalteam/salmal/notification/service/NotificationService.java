package com.salmalteam.salmal.notification.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salmalteam.salmal.notification.dto.response.FindNotificationResponse;
import com.salmalteam.salmal.notification.dto.response.NotificationDto;
import com.salmalteam.salmal.notification.infra.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	public FindNotificationResponse findAll(Long memberId) {
		return new FindNotificationResponse(notificationRepository.findByMemberId(memberId).stream()
			.map(NotificationDto::create)
			.collect(Collectors.toList()));
	}

	public void delete(Long id, String uuid) {

	}
}

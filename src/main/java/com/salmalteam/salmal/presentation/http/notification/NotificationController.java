package com.salmalteam.salmal.presentation.http.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.notification.dto.response.FindNotificationResponse;
import com.salmalteam.salmal.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/api/alarm")
	public ResponseEntity<FindNotificationResponse> findAlarms(@LoginMember MemberPayLoad memberPayLoad) {
		return ResponseEntity.ok(notificationService.findAll(memberPayLoad.getId()));
	}
}

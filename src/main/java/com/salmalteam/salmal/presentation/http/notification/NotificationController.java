package com.salmalteam.salmal.presentation.http.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.Login;
import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.notification.dto.request.ReadNotificationRequest;
import com.salmalteam.salmal.notification.dto.response.FindNotificationResponse;
import com.salmalteam.salmal.notification.service.DeleteNotificationRequest;
import com.salmalteam.salmal.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/api/notification")
	@Login
	public ResponseEntity<FindNotificationResponse> findAlarms(@LoginMember MemberPayLoad memberPayLoad) {
		return ResponseEntity.ok(notificationService.findAll(memberPayLoad.getId()));
	}

	@DeleteMapping("/api/notification")
	@Login
	public ResponseEntity<?> delete(@LoginMember MemberPayLoad memberPayLoad, DeleteNotificationRequest request) {
		notificationService.delete(memberPayLoad.getId(), request.getUuid());
		return ResponseEntity.ok()
			.build();
	}

	@PostMapping("api/notification/read")
	@Login
	public ResponseEntity<?> readNotification(@LoginMember MemberPayLoad memberPayLoad, ReadNotificationRequest request) {
		notificationService.read(memberPayLoad.getId(), request.getUuid());
		return ResponseEntity.ok()
			.build();
	}
}

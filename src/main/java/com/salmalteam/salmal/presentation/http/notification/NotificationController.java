package com.salmalteam.salmal.presentation.http.notification;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.LoginMember;
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
	public ResponseEntity<FindNotificationResponse> findAlarms(@LoginMember Long memberId) {
		return ResponseEntity.ok(notificationService.findAll(memberId));
	}

	@DeleteMapping("/api/notification")
	public ResponseEntity<?> delete(@LoginMember Long memberId, @RequestBody @Valid DeleteNotificationRequest request) {
		notificationService.delete(memberId, request.getUuid());
		return ResponseEntity.ok()
			.build();
	}

	@PostMapping("api/notification/read")
	public ResponseEntity<?> readNotification(@LoginMember Long memberId, @RequestBody @Valid ReadNotificationRequest request) {
		notificationService.read(memberId, request.getUuid());
		return ResponseEntity.ok()
			.build();
	}
}

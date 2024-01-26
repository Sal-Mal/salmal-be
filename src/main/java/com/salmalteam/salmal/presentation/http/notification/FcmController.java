package com.salmalteam.salmal.presentation.http.notification;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.Login;
import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.fcm.dto.request.AddFcmRequest;
import com.salmalteam.salmal.notification.service.MemberNotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FcmController {
	private final MemberNotificationService memberNotificationService;

	@PostMapping("/api/fcm/add-token")
	@Login
	public ResponseEntity<?> addFcm(@LoginMember MemberPayLoad memberPayLoad,
		@RequestBody @Valid AddFcmRequest addFcmRequest) {
		memberNotificationService.addToken(memberPayLoad.getId(), addFcmRequest.getToken());
		return ResponseEntity.ok()
			.build();
	}
}

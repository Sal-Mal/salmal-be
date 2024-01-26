package com.salmalteam.salmal.fcm.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddFcmRequest {
	@NotBlank(message = "FCM 토큰은 필수적으로 필요합니다.")
	private String token;
}

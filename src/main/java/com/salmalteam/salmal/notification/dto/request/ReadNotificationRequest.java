package com.salmalteam.salmal.notification.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReadNotificationRequest {

	@NotBlank(message = "알림의 고유번호는 필수로 필요합니다.")
	private String uuid;

}

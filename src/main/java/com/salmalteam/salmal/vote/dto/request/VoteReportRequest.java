package com.salmalteam.salmal.vote.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VoteReportRequest {
	@NotBlank(message = "신고사유는 필수적으로 필요합니다.")
	private String reason;
}

package com.salmalteam.salmal.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberVoteParticipantsDto {
	private Long id;
	private String nickname;
	private String profileImageUrl;
}

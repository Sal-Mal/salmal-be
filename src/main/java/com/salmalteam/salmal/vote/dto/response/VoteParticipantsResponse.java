package com.salmalteam.salmal.vote.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VoteParticipantsResponse {
	private List<MemberVoteParticipantsDto> memberVoteParticipants;
}

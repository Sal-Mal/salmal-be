package com.salmalteam.salmal.vote.entity.evaluation;

import java.util.List;

import com.salmalteam.salmal.vote.dto.response.MemberVoteParticipantsDto;

public interface VoteEvaluationCustom {
	List<MemberVoteParticipantsDto> findVoteMemberVoteParticipants(Long voteId);
}

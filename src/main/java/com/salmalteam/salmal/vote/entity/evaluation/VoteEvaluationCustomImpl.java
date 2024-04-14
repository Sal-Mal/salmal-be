package com.salmalteam.salmal.vote.entity.evaluation;

import static com.querydsl.core.types.Projections.*;
import static com.salmalteam.salmal.member.entity.QMember.*;
import static com.salmalteam.salmal.vote.entity.evaluation.QVoteEvaluation.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.vote.dto.response.MemberVoteParticipantsDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VoteEvaluationCustomImpl implements VoteEvaluationCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<MemberVoteParticipantsDto> findVoteMemberVoteParticipants(Long voteId) {
		return jpaQueryFactory.select(fields(MemberVoteParticipantsDto.class,
				voteEvaluation.evaluator.id, voteEvaluation.evaluator.nickName.value.as("nickname"),
				voteEvaluation.evaluator.memberImage.imageUrl.as("profileImageUrl")))
			.from(voteEvaluation)
			.leftJoin(voteEvaluation.evaluator, member)
			.where(voteEvaluation.vote.id.eq(voteId))
			.fetch();
	}
}

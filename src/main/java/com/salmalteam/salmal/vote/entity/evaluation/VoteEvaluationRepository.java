package com.salmalteam.salmal.vote.entity.evaluation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;

public interface VoteEvaluationRepository extends Repository<VoteEvaluation, Long>, VoteEvaluationCustom {
	VoteEvaluation save(VoteEvaluation voteEvaluation);

	void deleteByEvaluatorAndVote(Member member, Vote vote);

	Optional<VoteEvaluation> findByEvaluatorAndVote(Member member, Vote vote);

	boolean existsByEvaluatorAndVoteAndVoteEvaluationType(Member member, Vote vote,
		VoteEvaluationType voteEvaluationType);

	List<VoteEvaluation> findAllByEvaluator_Id(Long memberId);
}

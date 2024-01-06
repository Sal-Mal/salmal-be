package com.salmalteam.salmal.vote.entity.evaluation;

import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface VoteEvaluationRepository extends Repository<VoteEvaluation, Long> {
    VoteEvaluation save(VoteEvaluation voteEvaluation);
    void deleteByEvaluatorAndVote(Member member, Vote vote);
    Optional<VoteEvaluation> findByEvaluatorAndVote(Member member, Vote vote);
    boolean existsByEvaluatorAndVoteAndVoteEvaluationType(Member member, Vote vote, VoteEvaluationType voteEvaluationType);
    List<VoteEvaluation> findAllByEvaluator_Id(Long memberId);
}

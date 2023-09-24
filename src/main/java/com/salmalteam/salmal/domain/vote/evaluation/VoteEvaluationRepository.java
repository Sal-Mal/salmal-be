package com.salmalteam.salmal.domain.vote.evaluation;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface VoteEvaluationRepository extends Repository<VoteEvaluation, Long> {
    VoteEvaluation save(VoteEvaluation voteEvaluation);
    void deleteByEvaluatorAndVote(Member member, Vote vote);
    Optional<VoteEvaluation> findByEvaluatorAndVote(Member member, Vote vote);
    boolean existsByEvaluatorAndVoteAndVoteEvaluationType(Member member, Vote vote, VoteEvaluationType voteEvaluationType);
}

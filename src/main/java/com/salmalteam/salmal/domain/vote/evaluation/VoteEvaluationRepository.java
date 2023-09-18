package com.salmalteam.salmal.domain.vote.evaluation;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import org.springframework.data.repository.Repository;

public interface VoteEvaluationRepository extends Repository<VoteEvaluation, Long> {
    VoteEvaluation save(VoteEvaluation voteEvaluation);
    boolean existsByEvaluatorAndVoteAndVoteEvaluationType(Member member, Vote vote, VoteEvaluationType voteEvaluationType);

}

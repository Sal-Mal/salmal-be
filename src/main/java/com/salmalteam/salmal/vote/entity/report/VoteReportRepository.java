package com.salmalteam.salmal.vote.entity.report;

import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;

import org.springframework.data.repository.Repository;

public interface VoteReportRepository extends Repository<VoteReport, Long> {
    VoteReport save(VoteReport voteReport);
    Boolean existsByVoteAndReporter(Vote vote, Member member);
}

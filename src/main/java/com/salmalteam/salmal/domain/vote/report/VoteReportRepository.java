package com.salmalteam.salmal.domain.vote.report;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import org.springframework.data.repository.Repository;

public interface VoteReportRepository extends Repository<VoteReport, Long> {
    VoteReport save(VoteReport voteReport);
    Boolean existsByVoteAndReporter(Vote vote, Member member);
}

package com.salmalteam.salmal.domain.vote.entity.report;

import com.salmalteam.salmal.domain.member.entity.Member;
import com.salmalteam.salmal.domain.vote.entity.Vote;
import org.springframework.data.repository.Repository;

public interface VoteReportRepository extends Repository<VoteReport, Long> {
    VoteReport save(VoteReport voteReport);
    Boolean existsByVoteAndReporter(Vote vote, Member member);
}

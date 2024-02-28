package com.salmalteam.salmal.vote.entity.report;

import org.springframework.data.repository.Repository;

import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;

public interface VoteReportRepository extends Repository<VoteReport, Long>, VoteReportRepositoryCustom {
	VoteReport save(VoteReport voteReport);

	Boolean existsByVoteAndReporter(Vote vote, Member member);
}

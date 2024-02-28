package com.salmalteam.salmal.vote.entity.report;

import static com.salmalteam.salmal.vote.entity.report.QVoteReport.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class VoteReportRepositoryCustomImpl implements VoteReportRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	public VoteReportRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<Long> findReportVoteIdByMemberId(Long memberId) {
		return queryFactory.select(voteReport.vote.id)
			.from(voteReport)
			.where(voteReport.reporter.id.eq(memberId))
			.fetch();
	}
}

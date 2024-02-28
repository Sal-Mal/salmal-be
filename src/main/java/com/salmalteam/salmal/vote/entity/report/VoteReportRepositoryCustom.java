package com.salmalteam.salmal.vote.entity.report;

import java.util.List;

public interface VoteReportRepositoryCustom {
	List<Long> findReportVoteIdByMemberId(Long memberId);
}

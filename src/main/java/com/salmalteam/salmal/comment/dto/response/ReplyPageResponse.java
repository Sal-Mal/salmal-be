package com.salmalteam.salmal.comment.dto.response;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.function.Predicate;

import lombok.Getter;

@Getter
public class ReplyPageResponse {
	private boolean hasNext;
	private List<ReplyResponse> comments;

	private ReplyPageResponse(boolean hashNext, List<ReplyResponse> comments) {
		this.hasNext = hashNext;
		this.comments = comments;
	}

	public static ReplyPageResponse of(boolean hashNext, List<ReplyResponse> comments) {
		return new ReplyPageResponse(hashNext, comments);
	}

	public void filteringBlockedMembers(List<Long> ids) {
		comments = comments.stream()
			.filter(filterBlockedMemberPredicate(ids))
			.collect(toList());
	}

	private Predicate<ReplyResponse> filterBlockedMemberPredicate(List<Long> ids) {
		return voteResponse -> ids.stream()
			.noneMatch(id -> id.equals(voteResponse.getMemberId()));
	}
}

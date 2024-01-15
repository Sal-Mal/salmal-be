package com.salmalteam.salmal.vote.dto.response;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VotePageResponse {

	private boolean hasNext;
	private List<VoteResponse> votes;

	private VotePageResponse(final boolean hasNext, final List<VoteResponse> voteResponses) {
		this.hasNext = hasNext;
		this.votes = voteResponses;
	}

	public static VotePageResponse of(final boolean hasNext, final List<VoteResponse> voteResponses) {
		return new VotePageResponse(hasNext, voteResponses);
	}

	public void filteringBlockedMembers(List<Long> ids) {
		votes = votes.stream()
			.filter(filterBlockedMemberPredicate(ids))
			.collect(toList());
	}

	public Predicate<VoteResponse> filterBlockedMemberPredicate(List<Long> ids) {
		return voteResponse -> ids.stream()
			.noneMatch(id -> id.equals(voteResponse.getMemberId()));
	}

}

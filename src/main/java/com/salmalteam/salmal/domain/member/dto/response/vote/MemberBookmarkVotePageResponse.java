package com.salmalteam.salmal.domain.member.dto.response.vote;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberBookmarkVotePageResponse {
    private boolean hasNext;
    private List<MemberBookmarkVoteResponse> votes;

    public MemberBookmarkVotePageResponse(boolean hasNext, List<MemberBookmarkVoteResponse> votes) {
        this.hasNext = hasNext;
        this.votes = votes;
    }

    public static MemberBookmarkVotePageResponse of(boolean hasNext, List<MemberBookmarkVoteResponse> votes){
        return new MemberBookmarkVotePageResponse(hasNext, votes);
    }
}

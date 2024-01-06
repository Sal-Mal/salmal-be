package com.salmalteam.salmal.member.dto.response.vote;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberVotePageResponse {
    private boolean hasNext;
    private List<MemberVoteResponse> votes;

    private MemberVotePageResponse(boolean hasNext, List<MemberVoteResponse> votes) {
        this.hasNext = hasNext;
        this.votes = votes;
    }
    public static MemberVotePageResponse of(boolean hasNest, List<MemberVoteResponse> votes){
        return new MemberVotePageResponse(hasNest, votes);
    }
}

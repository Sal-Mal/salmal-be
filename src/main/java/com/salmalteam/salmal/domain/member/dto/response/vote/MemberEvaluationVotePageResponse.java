package com.salmalteam.salmal.domain.member.dto.response.vote;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberEvaluationVotePageResponse {
    private boolean hasNext;
    private List<MemberEvaluationVoteResponse> votes;

    private MemberEvaluationVotePageResponse(boolean hasNext, List<MemberEvaluationVoteResponse> votes) {
        this.hasNext = hasNext;
        this.votes = votes;
    }

    public static MemberEvaluationVotePageResponse of(boolean hasNext, List<MemberEvaluationVoteResponse> votes){
        return new MemberEvaluationVotePageResponse(hasNext, votes);
    }
}

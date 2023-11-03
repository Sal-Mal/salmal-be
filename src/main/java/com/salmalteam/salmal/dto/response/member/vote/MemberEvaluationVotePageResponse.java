package com.salmalteam.salmal.dto.response.member.vote;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
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

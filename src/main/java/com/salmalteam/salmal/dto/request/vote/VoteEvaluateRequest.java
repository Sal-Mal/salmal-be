package com.salmalteam.salmal.dto.request.vote;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteEvaluateRequest {

    @NotNull(message = "투표 타입을 입력해주세요")
    private String voteEvaluationType;
    public VoteEvaluateRequest(final String voteEvaluationType){
        this.voteEvaluationType = voteEvaluationType;
    }
}

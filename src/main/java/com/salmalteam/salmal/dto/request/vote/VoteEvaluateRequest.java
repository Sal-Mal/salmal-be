package com.salmalteam.salmal.dto.request.vote;

import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteEvaluateRequest {

    @NotNull(message = "투표 타입을 입력해주세요(like, disLike)")
    private VoteEvaluationType voteEvaluationType;
    public VoteEvaluateRequest(final String voteEvaluationType){
        this.voteEvaluationType = VoteEvaluationType.from(voteEvaluationType);
    }
}

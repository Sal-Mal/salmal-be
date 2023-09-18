package com.salmalteam.salmal.domain.vote.evaluation;

import com.salmalteam.salmal.exception.vote.VoteException;
import com.salmalteam.salmal.exception.vote.VoteExceptionType;

import java.util.Arrays;

public enum VoteEvaluationType {
    LIKE,
    DISLIKE;

    public static VoteEvaluationType from(final String voteEvaluationType){
        return Arrays.stream(VoteEvaluationType.values())
                .filter(it -> it.name().equalsIgnoreCase(voteEvaluationType))
                .findAny()
                .orElseThrow(() -> new VoteException(VoteExceptionType.INVALID_VOTE_EVALUATION_TYPE));
    }
}

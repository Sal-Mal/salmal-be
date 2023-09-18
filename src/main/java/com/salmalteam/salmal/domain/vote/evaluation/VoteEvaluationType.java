package com.salmalteam.salmal.domain.vote.evaluation;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum VoteEvaluationType {
    LIKE,
    DISLIKE;

    @JsonCreator
    public static VoteEvaluationType from(final String voteEvaluationType){
        return Arrays.stream(VoteEvaluationType.values())
                .filter(it -> it.name().equalsIgnoreCase(voteEvaluationType))
                .findFirst()
                .orElse(null);
    }
}

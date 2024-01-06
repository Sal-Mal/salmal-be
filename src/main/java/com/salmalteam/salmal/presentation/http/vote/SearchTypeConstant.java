package com.salmalteam.salmal.presentation.http.vote;

import com.salmalteam.salmal.domain.vote.exception.VoteException;
import com.salmalteam.salmal.domain.vote.exception.VoteExceptionType;

import java.util.Arrays;

public enum SearchTypeConstant {
    BEST,
    HOME;
    public static SearchTypeConstant from(final String searchType){
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(searchType))
                .findAny()
                .orElseThrow(() -> new VoteException(VoteExceptionType.INVALID_VOTE_SEARCH_TYPE));
    }
}

package com.salmalteam.salmal.domain.vote.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VotePageResponse {

    private boolean hasNext;
    private List<VoteResponse> votes;

    private VotePageResponse(final boolean hasNext,final List<VoteResponse> voteResponses) {
        this.hasNext = hasNext;
        this.votes = voteResponses;
    }
    public static VotePageResponse of(final boolean hasNext, final List<VoteResponse> voteResponses){
        return new VotePageResponse(hasNext, voteResponses);
    }

}

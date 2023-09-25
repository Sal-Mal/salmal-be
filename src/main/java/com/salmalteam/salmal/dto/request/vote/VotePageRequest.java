package com.salmalteam.salmal.dto.request.vote;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VotePageRequest {

    private final int DEFAULT_SIZE = 8;

    private Long cursorId;
    private Integer cursorLikes;
    private Integer size;
    private String searchType;

    public VotePageRequest(final Long cursorId, final Integer cursorLikes, final Integer size, final String searchType) {
        this.cursorId = cursorId;
        this.cursorLikes = cursorLikes;
        this.size = size == null ? DEFAULT_SIZE : size;
        this.searchType = searchType;
    }
}

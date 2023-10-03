package com.salmalteam.salmal.dto.request.member.vote;

import lombok.Getter;

@Getter
public class MemberVotePageRequest {

    private final int DEFAULT_SIZE = 8;

    private Long cursorId;
    private Integer size;

    private MemberVotePageRequest(Long cursorId, Integer size) {
        this.cursorId = cursorId;
        this.size = size == null ? DEFAULT_SIZE : size;
    }
    public static MemberVotePageRequest of(Long cursorId, Integer size){
        return new MemberVotePageRequest(cursorId, size);
    }
}

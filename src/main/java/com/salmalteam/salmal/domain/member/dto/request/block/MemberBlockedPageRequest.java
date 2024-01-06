package com.salmalteam.salmal.domain.member.dto.request.block;

import lombok.Getter;

@Getter
public class MemberBlockedPageRequest {
    private final int DEFAULT_SIZE = 8;

    private Long cursorId;
    private Integer size;

    private MemberBlockedPageRequest(final Long cursorId,final Integer size) {
        this.cursorId = cursorId;
        this.size = size == null ? DEFAULT_SIZE : size;
    }

    public static MemberBlockedPageRequest of(final Long cursorId,final Integer size){
        return new MemberBlockedPageRequest(cursorId, size);
    }

}

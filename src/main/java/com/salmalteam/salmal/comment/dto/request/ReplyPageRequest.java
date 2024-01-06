package com.salmalteam.salmal.comment.dto.request;

import lombok.Getter;

@Getter
public class ReplyPageRequest {

    private final int DEFAULT_SIZE = 8;

    private Long cursorId;
    private Integer size;

    private ReplyPageRequest(Long cursorId, Integer size) {
        this.cursorId = cursorId;
        this.size = size == null ? DEFAULT_SIZE : DEFAULT_SIZE;
    }
    public static ReplyPageRequest of(final Long cursorId, final Integer size){
        return new ReplyPageRequest(cursorId, size);
    }
}

package com.salmalteam.salmal.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentPageRequest {

    private final int DEFAULT_SIZE = 8;

    private Long cursorId;
    private Integer size;
    private CommentPageRequest(Long cursorId, Integer size) {
        this.cursorId = cursorId;
        this.size = size == null ? DEFAULT_SIZE : size;
    }
    public static CommentPageRequest of(final Long cursorId, final Integer size){
        return new CommentPageRequest(cursorId, size);
    }
}

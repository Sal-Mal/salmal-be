package com.salmalteam.salmal.comment.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ReplyPageResponse {
    private boolean hasNext;
    private List<ReplyResponse> comments;

    private ReplyPageResponse(boolean hashNext, List<ReplyResponse> comments) {
        this.hasNext = hashNext;
        this.comments = comments;
    }

    public static ReplyPageResponse of(boolean hashNext, List<ReplyResponse> comments){
        return new ReplyPageResponse(hashNext, comments);
    }
}

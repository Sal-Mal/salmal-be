package com.salmalteam.salmal.dto.response.comment;

import lombok.Getter;

import java.util.List;

@Getter
public class ReplyPageResponse {
    private boolean hasNext;
    private List<ReplyResponse> replies;

    private ReplyPageResponse(boolean hashNext, List<ReplyResponse> replies) {
        this.hasNext = hashNext;
        this.replies = replies;
    }

    public static ReplyPageResponse of(boolean hashNext, List<ReplyResponse> replies){
        return new ReplyPageResponse(hashNext, replies);
    }
}

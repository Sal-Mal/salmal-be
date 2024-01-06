package com.salmalteam.salmal.domain.comment.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentPageResponse {
    private boolean hasNext;
    private List<CommentResponse> comments;
    private CommentPageResponse(boolean hasNext, List<CommentResponse> comments) {
        this.hasNext = hasNext;
        this.comments = comments;
    }

    public static CommentPageResponse of(boolean hasNext, List<CommentResponse> comments){
        return new CommentPageResponse(hasNext, comments);
    }
}
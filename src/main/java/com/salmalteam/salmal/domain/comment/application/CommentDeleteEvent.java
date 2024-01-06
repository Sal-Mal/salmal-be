package com.salmalteam.salmal.domain.comment.application;

import com.salmalteam.salmal.domain.comment.entity.CommentType;
import lombok.Getter;

@Getter
public class CommentDeleteEvent {
    private final Long commentId;
    private final CommentType commentType;
    private CommentDeleteEvent(final Long commentId, final CommentType commentType){
        this.commentId = commentId;
        this.commentType = commentType;
    }
    public static CommentDeleteEvent of(final Long commentId, final CommentType commentType){
        return new CommentDeleteEvent(commentId, commentType);
    }
}

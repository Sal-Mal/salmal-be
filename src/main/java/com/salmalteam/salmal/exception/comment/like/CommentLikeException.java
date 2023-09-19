package com.salmalteam.salmal.exception.comment.like;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class CommentLikeException extends CustomException {
    public CommentLikeException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

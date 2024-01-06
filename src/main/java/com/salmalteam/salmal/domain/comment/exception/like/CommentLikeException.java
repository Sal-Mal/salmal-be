package com.salmalteam.salmal.domain.comment.exception.like;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class CommentLikeException extends CustomException {
    public CommentLikeException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

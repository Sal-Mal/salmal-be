package com.salmalteam.salmal.comment.exception;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class CommentException extends CustomException {

    public CommentException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

package com.salmalteam.salmal.exception.comment;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class CommentException extends CustomException {

    public CommentException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

package com.salmalteam.salmal.exception.vote.bookmark;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class VoteBookmarkException extends CustomException {
    public VoteBookmarkException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

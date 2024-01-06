package com.salmalteam.salmal.vote.exception.bookmark;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class VoteBookmarkException extends CustomException {
    public VoteBookmarkException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

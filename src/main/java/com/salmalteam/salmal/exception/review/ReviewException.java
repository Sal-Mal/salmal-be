package com.salmalteam.salmal.exception.review;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class ReviewException extends CustomException {

    public ReviewException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

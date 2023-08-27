package com.salmalteam.salmal.exception.review.like;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class ReviewLikeException extends CustomException {
    public ReviewLikeException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

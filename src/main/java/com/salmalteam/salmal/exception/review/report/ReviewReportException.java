package com.salmalteam.salmal.exception.review.report;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class ReviewReportException extends CustomException {

    public ReviewReportException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

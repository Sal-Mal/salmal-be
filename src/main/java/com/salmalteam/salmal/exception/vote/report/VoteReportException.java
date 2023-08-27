package com.salmalteam.salmal.exception.vote.report;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class VoteReportException extends CustomException {
    public VoteReportException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

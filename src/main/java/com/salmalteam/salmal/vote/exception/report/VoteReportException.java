package com.salmalteam.salmal.vote.exception.report;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class VoteReportException extends CustomException {
    public VoteReportException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

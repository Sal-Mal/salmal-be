package com.salmalteam.salmal.comment.exception.report;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class CommentReportException extends CustomException {

    public CommentReportException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

package com.salmalteam.salmal.exception.comment.report;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class CommentReportException extends CustomException {

    public CommentReportException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

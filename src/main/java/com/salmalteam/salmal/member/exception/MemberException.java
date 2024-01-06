package com.salmalteam.salmal.member.exception;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class MemberException extends CustomException {

    public MemberException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

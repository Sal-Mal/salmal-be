package com.salmalteam.salmal.exception.member;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class MemberException extends CustomException {

    public MemberException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

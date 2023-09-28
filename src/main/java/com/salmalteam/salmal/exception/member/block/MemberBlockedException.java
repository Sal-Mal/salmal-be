package com.salmalteam.salmal.exception.member.block;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class MemberBlockedException extends CustomException {
    public MemberBlockedException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

package com.salmalteam.salmal.member.exception.block;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class MemberBlockedException extends CustomException {
    public MemberBlockedException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

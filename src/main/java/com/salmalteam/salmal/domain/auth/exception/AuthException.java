package com.salmalteam.salmal.domain.auth.exception;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class AuthException extends CustomException {
    public AuthException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

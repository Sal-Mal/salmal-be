package com.salmalteam.salmal.exception.auth;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class AuthException extends CustomException {
    public AuthException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

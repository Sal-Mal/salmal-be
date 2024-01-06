package com.salmalteam.salmal.domain.vote.exception;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class VoteException extends CustomException {
    public VoteException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

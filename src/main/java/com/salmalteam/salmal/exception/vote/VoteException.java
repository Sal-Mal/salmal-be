package com.salmalteam.salmal.exception.vote;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class VoteException extends CustomException {
    public VoteException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}

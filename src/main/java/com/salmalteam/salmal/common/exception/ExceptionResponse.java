package com.salmalteam.salmal.common.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private final String message;
    private final int code;

    private ExceptionResponse(final String message, final int code){
        this.message = message;
        this.code = code;
    }

    public static ExceptionResponse of(final String message, final int code){
        return new ExceptionResponse(message, code);
    }

}

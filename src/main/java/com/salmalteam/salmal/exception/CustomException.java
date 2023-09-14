package com.salmalteam.salmal.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ExceptionType exceptionType;
    public CustomException(final ExceptionType exceptionType){
        super(String.format("%s -> [ type = %s ] [ code = %s ] ", exceptionType.getServerMessage(), exceptionType.getStatus(), exceptionType.getCode()));
        this.exceptionType = exceptionType;
    }
}

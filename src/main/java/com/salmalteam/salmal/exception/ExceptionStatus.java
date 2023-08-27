package com.salmalteam.salmal.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum ExceptionStatus {

    NOT_FOUND(Status.NOT_FOUND, HttpStatus.NOT_FOUND),
    UNAUTHORIZED(Status.UNAUTHORIZED, HttpStatus.UNAUTHORIZED),
    FORBIDDEN(Status.FORBIDDEN, HttpStatus.FORBIDDEN),
    BAD_REQUEST(Status.BAD_REQUEST, HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(Status.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final Status status;
    private final HttpStatus httpStatus;

    ExceptionStatus(final Status status, final HttpStatus httpStatus){
        this.status = status;
        this.httpStatus = httpStatus;
    }

    public static ExceptionStatus from(final Status status){
        return Arrays.stream(ExceptionStatus.values())
                .filter(it -> it.status == status)
                .findAny()
                .orElse(INTERNAL_SERVER_ERROR);
    }

}

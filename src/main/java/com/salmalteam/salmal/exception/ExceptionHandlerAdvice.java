package com.salmalteam.salmal.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(final CustomException e){
        log.warn(e.getMessage(), e);
        ExceptionType exceptionType = e.getExceptionType();
        ExceptionStatus exceptionStatus = ExceptionStatus.from(exceptionType.getStatus());
        return ResponseEntity.status(exceptionStatus.getHttpStatus())
                .body(ExceptionResponse.of(exceptionType.getClientMessage(), exceptionType.getCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(final Exception e){
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of(e.getMessage(), 9999));
    }
}

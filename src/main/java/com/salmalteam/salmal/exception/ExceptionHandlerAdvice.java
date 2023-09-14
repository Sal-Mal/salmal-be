package com.salmalteam.salmal.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(final CustomException e){
        log.warn(e.getMessage(), e);
        final ExceptionType exceptionType = e.getExceptionType();
        final ExceptionStatus exceptionStatus = ExceptionStatus.from(exceptionType.getStatus());
        return ResponseEntity.status(exceptionStatus.getHttpStatus())
                .body(ExceptionResponse.of(exceptionType.getClientMessage(), exceptionType.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionResponse>> handleBindException(final MethodArgumentNotValidException e){
        log.warn(e.getMessage(), e);

        final List<ExceptionResponse> exceptionResponses = createExceptionResponses(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponses);
    }

    private List<ExceptionResponse> createExceptionResponses(final MethodArgumentNotValidException e){
        return e.getFieldErrors()
                .stream()
                .map(it -> ExceptionResponse.of(it.getDefaultMessage(), 0000))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e){
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.of(e.getMessage(), 9999));
    }
}

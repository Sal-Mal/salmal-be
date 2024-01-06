package com.salmalteam.salmal.image.exception;

import com.salmalteam.salmal.common.exception.CustomException;
import com.salmalteam.salmal.common.exception.ExceptionType;

public class ImageException extends CustomException {
    public ImageException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

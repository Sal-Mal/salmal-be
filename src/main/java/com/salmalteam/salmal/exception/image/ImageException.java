package com.salmalteam.salmal.exception.image;

import com.salmalteam.salmal.exception.CustomException;
import com.salmalteam.salmal.exception.ExceptionType;

public class ImageException extends CustomException {
    public ImageException(final ExceptionType exceptionType) {
        super(exceptionType);
    }
}

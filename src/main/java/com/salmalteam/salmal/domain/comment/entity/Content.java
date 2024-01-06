package com.salmalteam.salmal.domain.comment.entity;

import com.salmalteam.salmal.domain.comment.exception.CommentException;
import com.salmalteam.salmal.domain.comment.exception.CommentExceptionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    private static final int MAX_LENGTH = 100;
    private static final int MIN_LENGTH = 1;

    @Column
    private String value;

    private Content(final String value){
        validateContent(value);
        this.value = value;
    }

    public static Content of(final String value){
        return new Content(value);
    }

    private void validateContent(final String value){
        if(isNotValidLength(value)){
            throw new CommentException(CommentExceptionType.INVALID_COMMENT_LENGTH);
        }
    }

    private boolean isNotValidLength(final String value){
        return value.length() < MIN_LENGTH || value.length() > MAX_LENGTH;
    }
}

package com.salmalteam.salmal.domain.member.entity;

import com.salmalteam.salmal.domain.member.exception.MemberException;
import com.salmalteam.salmal.domain.member.exception.MemberExceptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Introduction {
    private static final int MIN_LENGTH = 0;
    private static final int MAX_LENGTH = 30;

    private static final String INITIAL_INTRO = "안녕하세요!";

    @Column(name = "introduction")
    private String value;

    private Introduction(final String value) {
        validateIntroduction(value);
        this.value = value;
    }
    public static Introduction from(final String value){
        return new Introduction(value);
    }

    public static Introduction initIntroduction(){
        return new Introduction(INITIAL_INTRO);
    }
    private void validateIntroduction(final String value){
        if(isNotValidLength(value)){
            throw new MemberException(MemberExceptionType.INVALID_INTRODUCTION_LENGTH);
        }
    }
    private boolean isNotValidLength(final String value){
        return value.length() < MIN_LENGTH || value.length() > MAX_LENGTH;
    }


}

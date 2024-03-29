package com.salmalteam.salmal.member.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.salmalteam.salmal.member.exception.MemberException;
import com.salmalteam.salmal.member.exception.MemberExceptionType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NickName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 20;

    @Column(name = "nickName", nullable = false, unique = true)
    private String value;

    private NickName(final String value) {
        validateNickName(value);
        this.value = value;
    }

    public static NickName from(final String value) {
        return new NickName(value);
    }

	private void validateNickName(final String value) {
        if (isNotValidLength(value)) {
            throw new MemberException(MemberExceptionType.INVALID_NICKNAME_LENGTH);
        }
    }
    private boolean isNotValidLength(String value) {
        return value.length() < MIN_LENGTH || value.length() > MAX_LENGTH;
    }
}

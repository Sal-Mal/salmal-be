package com.salmalteam.salmal.exception.auth;

import com.salmalteam.salmal.exception.ExceptionType;
import com.salmalteam.salmal.exception.Status;

public enum AuthExceptionType implements ExceptionType {
    NOT_SUPPORTED_LOGIN_TYPE(Status.BAD_REQUEST, 4001, "지원하지 않는 로그인 방식입니다.", "지원하지 않는 방식(provider)의 로그인 요청"),
    NOT_FOUND(Status.NOT_FOUND, 4002, "해당하는 재토큰이 존재하지 않습니다.", "존재하지 않는 재발급 토큰 요청"),
    NOT_VALID_REFRESH_TOKEN(Status.BAD_REQUEST, 4003, "재발급 토큰이 유효하지 않습니다.", "유효하지 않은 재발급 토큰 요청")
    ;

    private final Status status;
    private final int code;
    private String clientMessage;
    private String serverMessage;

    AuthExceptionType(final Status status, final int code, final String clientMessage, final String serverMessage) {
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getClientMessage() {
        return clientMessage;
    }

    @Override
    public String getServerMessage() {
        return serverMessage;
    }
}

package com.salmalteam.salmal.exception.auth;

import com.salmalteam.salmal.exception.ExceptionType;
import com.salmalteam.salmal.exception.Status;

public enum AuthExceptionType implements ExceptionType {
    NOT_SUPPORTED_LOGIN_TYPE(Status.BAD_REQUEST, 4001, "지원하지 않는 로그인 방식입니다.", "지원하지 않는 방식(provider)의 로그인 요청")
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

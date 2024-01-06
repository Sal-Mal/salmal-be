package com.salmalteam.salmal.domain.member.exception.block;

import com.salmalteam.salmal.common.exception.ExceptionType;
import com.salmalteam.salmal.common.exception.Status;

public enum MemberBlockedExceptionType implements ExceptionType {

    DUPLICATED_MEMBER_BLOCKED(Status.BAD_REQUEST, 1101, "이미 차단한 회원입니다.", "중복된 회원 차단 요청"),
    NOT_FOUND(Status.NOT_FOUND, 1102, "해당 회원을 차단한 이력이 존재하지 않습니다.", "존재하지 않는 회원 차단 요청"),
    FORBIDDEN_SEARCH(Status.FORBIDDEN, 1103, "차단 목록은 본인만 조회할 수 있습니다.", "권한이 없는 차단 목록 조회 요청");
    ;
    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    MemberBlockedExceptionType(final Status status, final int code, final String clientMessage, final String serverMessage) {
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

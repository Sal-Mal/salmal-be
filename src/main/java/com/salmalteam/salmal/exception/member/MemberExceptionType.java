package com.salmalteam.salmal.exception.member;

import com.salmalteam.salmal.exception.ExceptionType;
import com.salmalteam.salmal.exception.Status;

public enum MemberExceptionType implements ExceptionType {
    NOT_FOUND(Status.NOT_FOUND, 1001, "회원을 찾을 수가 없습니다.", "존재하지 않는 회원 요청"),
    FORBIDDEN_DELETE(Status.FORBIDDEN, 1002, "회원 탈퇴할 권한이 없습니다. 회원 탈퇴는 본인만이 할 수 있습니다.", "권한이 없는 회원 탈퇴 요청"),
    FORBIDDEN_UPDATE(Status.FORBIDDEN, 1003, "회원 정보를 수정할 권한이 없습니다. 회원 정보는 본인만이 수정할 수 있습니다.", "수정 권한이 없는 회원 정보 수정 요청"),
    INVALID_NICKNAME_LENGTH(Status.BAD_REQUEST, 1004, "닉네임의 최소 길이는 2, 최대 길이는 20입니다. ", "잘못된 길이의 회원 닉네임 요청"),
    ;

    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    MemberExceptionType(final Status status, final int code, String clientMessage, String serverMessage){
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

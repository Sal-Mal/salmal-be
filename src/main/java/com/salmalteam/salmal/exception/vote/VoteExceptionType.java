package com.salmalteam.salmal.exception.vote;

import com.salmalteam.salmal.exception.ExceptionType;
import com.salmalteam.salmal.exception.Status;

public enum VoteExceptionType implements ExceptionType {
    NOT_FOUND(Status.NOT_FOUND, 3001, "투표를 찾을 수가 없습니다.", "존재하지 않는 투표 요청"),
    FORBIDDEN_DELETE(Status.FORBIDDEN, 3002, "투표를 삭제할 권한이 없습니다.", "권한이 없는 투표 삭제 요청"),
    FORBIDDEN_UPDATE(Status.FORBIDDEN, 3003, "투표를 수정할 권한이 없습니다.", "권한이 없는 투표 수정 요청"),
    ;

    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    VoteExceptionType(final Status status, final int code, String clientMessage, String serverMessage){
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

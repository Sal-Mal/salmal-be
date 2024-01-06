package com.salmalteam.salmal.domain.vote.exception.bookmark;

import com.salmalteam.salmal.common.exception.ExceptionType;
import com.salmalteam.salmal.common.exception.Status;

public enum VoteBookmarkExceptionType implements ExceptionType {

    DUPLICATED_BOOKMARK(Status.BAD_REQUEST, 3201, "이미 북마크한 투표입니다.", "중복 북마킹 요청")
    ;


    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    VoteBookmarkExceptionType(final Status status,final int code,final String clientMessage,final String serverMessage) {
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

package com.salmalteam.salmal.comment.exception.like;

import com.salmalteam.salmal.common.exception.ExceptionType;
import com.salmalteam.salmal.common.exception.Status;

public enum CommentLikeExceptionType implements ExceptionType {
    NOT_FOUND(Status.NOT_FOUND, 2101, "좋아요를 한 댓글이 아닙니다.", "존재하지 않는 좋아요 요청"),
    DUPLICATED_LIKE(Status.BAD_REQUEST, 2102, "이미 좋아요한 댓글입니다.", "중복된 좋아요 요청")
    ;

    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    CommentLikeExceptionType(final Status status, final int code, String clientMessage, String serverMessage){
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

package com.salmalteam.salmal.exception.comment.like;

import com.salmalteam.salmal.exception.ExceptionType;
import com.salmalteam.salmal.exception.Status;

public enum CommentLikeExceptionType implements ExceptionType {
    FORBIDDEN_DELETE(Status.FORBIDDEN, 2101, "좋아요를 취소할 권한이 없습니다. 좋아요 취소는 본인만이 할 수 있습니다.", "권한이 없는 좋아요 취소 요청")
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

package com.salmalteam.salmal.domain.comment.exception;

import com.salmalteam.salmal.common.exception.ExceptionType;
import com.salmalteam.salmal.common.exception.Status;

public enum CommentExceptionType implements ExceptionType {
    NOT_FOUND(Status.NOT_FOUND, 2001, "댓글을 찾을 수가 없습니다.", "존재하지 않는 리뷰 요청"),
    FORBIDDEN_DELETE(Status.FORBIDDEN, 2002, "댓글를 삭제할 권한이 없습니다.", "삭제 권한이 없는 리뷰 삭제 요청"),
    FORBIDDEN_UPDATE(Status.FORBIDDEN, 2003, "댓글를 수정할 권한이 없습니다.", "수정 권한이 없는 리뷰 수정 요청"),
    INVALID_COMMENT_LENGTH(Status.BAD_REQUEST, 2004, "댓글의 최소 길이는 1 최대 길이는 100입니다.", "잘못된 길이의 리뷰 요청(100자 초과)")
    ;

    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    CommentExceptionType(final Status status, final int code, String clientMessage, String serverMessage){
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

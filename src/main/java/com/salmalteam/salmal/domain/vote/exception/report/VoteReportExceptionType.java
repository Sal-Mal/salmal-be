package com.salmalteam.salmal.domain.vote.exception.report;

import com.salmalteam.salmal.common.exception.ExceptionType;
import com.salmalteam.salmal.common.exception.Status;

public enum VoteReportExceptionType implements ExceptionType {
    FORBIDDEN_DELETE(Status.FORBIDDEN, 3101, "리뷰를 삭제할 권한이 없습니다.", "삭제 권한이 없는 리뷰 삭제 요청"),
    DUPLICATED_REPORT(Status.BAD_REQUEST, 3101, "이미 신고한 투표입니다.", "중복된 투표 신고 요청")
    ;

    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    VoteReportExceptionType(final Status status, final int code, String clientMessage, String serverMessage){
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

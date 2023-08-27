package com.salmalteam.salmal.exception.review.report;

import com.salmalteam.salmal.exception.ExceptionType;
import com.salmalteam.salmal.exception.Status;

public enum ReviewReportExceptionType implements ExceptionType {
    DUPLICATED_REPORT(Status.BAD_REQUEST, 2201, "이미 신고한 리뷰입니다.", "동일 리뷰에 대한 중복 신고 요청")
    ;

    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    ReviewReportExceptionType(final Status status, final int code, String clientMessage, String serverMessage){
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

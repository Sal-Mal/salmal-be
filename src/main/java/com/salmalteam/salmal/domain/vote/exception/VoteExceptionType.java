package com.salmalteam.salmal.domain.vote.exception;

import com.salmalteam.salmal.common.exception.ExceptionType;
import com.salmalteam.salmal.common.exception.Status;

public enum VoteExceptionType implements ExceptionType {
    NOT_FOUND(Status.NOT_FOUND, 3001, "투표를 찾을 수가 없습니다.", "존재하지 않는 투표 요청"),
    FORBIDDEN_DELETE(Status.FORBIDDEN, 3002, "투표를 삭제할 권한이 없습니다.", "권한이 없는 투표 삭제 요청"),
    FORBIDDEN_UPDATE(Status.FORBIDDEN, 3003, "투표를 수정할 권한이 없습니다.", "권한이 없는 투표 수정 요청"),
    DUPLICATED_VOTE_EVALUATION(Status.BAD_REQUEST, 3004, "이미 평가를 한 투표입니다.", "중복된 투표 평가 요청"),
    INVALID_VOTE_EVALUATION_TYPE(Status.BAD_REQUEST, 3005, "투표타입은 LIKE 또는 DISLIKE 입니다.", "적절하지 않은 투표 타입 요청"),
    DUPLICATED_VOTE_REPORT(Status.BAD_REQUEST, 3006, "이미 신고한 투표입니다.", "중복 투표 신고 요청"),
    INVALID_VOTE_SEARCH_TYPE(Status.BAD_REQUEST, 3007, "잘못된 투표 검색 타입입니다. (BEST, HOME)", "잘못된 투표 검색 타입 요청")
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

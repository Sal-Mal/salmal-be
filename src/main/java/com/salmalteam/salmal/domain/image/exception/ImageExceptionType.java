package com.salmalteam.salmal.domain.image.exception;

import com.salmalteam.salmal.common.exception.ExceptionType;
import com.salmalteam.salmal.common.exception.Status;

public enum ImageExceptionType implements ExceptionType {

    NO_FILE_NAME(Status.BAD_REQUEST, 5001, "이미지 파일 이름이 없습니다.", "이름이 없는 이미지 파일 요청"),
    NO_FILE_EXTENSION(Status.BAD_REQUEST, 5002, "이미지 파일 확장자가 없습니다.", "확장자가 없는 이미지 파일 요청"),
    NOT_SUPPORT_FILE_EXTENSION(Status.BAD_REQUEST, 5003, "지원하는 형식(JPG, JPEG)의 이미지 파일 형식이 아닙니다.", "지원하지 않는 형식의 이미지 파일 요청"),
    INVALID_INPUT_STREAM(Status.INTERNAL_SERVER_ERROR, 5004, "파일의 데이터를 가져오지 못했습니다.", "파일 데이터(InputStream)을 가져오는데 실패"),
    IMAGE_FILE_UPLOAD_FAILED(Status.INTERNAL_SERVER_ERROR, 5005, "이미지 업로드에 실패했습니다.", "이미지 파일 업로드 실패"),
    NOT_IMAGE_FILE(Status.BAD_REQUEST, 5006, "이미지 파일이 아닙니다.", "이미지 파일이 아닌 요청"),
    IMAGE_FILE_READ_FAILED(Status.INTERNAL_SERVER_ERROR, 5006, "이미지 파일을 읽는데 실패했습니다.", "이미지 파일 읽기 실패")

    ;

    private final Status status;
    private final int code;
    private final String clientMessage;
    private final String serverMessage;

    ImageExceptionType(final Status status, final int code, final String clientMessage, final String serverMessage) {
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

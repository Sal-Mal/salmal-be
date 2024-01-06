package com.salmalteam.salmal.common.exception;

public interface ExceptionType {
    Status getStatus();
    int getCode();
    String getClientMessage();
    String getServerMessage();
}

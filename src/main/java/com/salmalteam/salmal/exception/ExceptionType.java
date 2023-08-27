package com.salmalteam.salmal.exception;

public interface ExceptionType {
    Status getStatus();
    int getCode();
    String getClientMessage();
    String getServerMessage();
}

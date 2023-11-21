package com.aroom.global.error;

import lombok.Getter;

@Getter
public abstract class ServiceException extends RuntimeException{

    private final ErrorCode errorCode;

    protected ServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

package com.fastcampus.mini9.domain.accommodation.exception.repository;

import com.fastcampus.mini9.common.exception.BaseException;
import com.fastcampus.mini9.common.exception.ErrorCode;

public class IllegalRequestException extends BaseException {
    public IllegalRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public IllegalRequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public IllegalRequestException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public IllegalRequestException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}

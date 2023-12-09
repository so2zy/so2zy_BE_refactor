package com.fastcampus.mini9.domain.accommodation.exception;

import com.fastcampus.mini9.common.exception.BaseException;
import com.fastcampus.mini9.common.exception.ErrorCode;

public class NoSuchAccommodationType extends BaseException {
    public NoSuchAccommodationType(ErrorCode errorCode) {
        super(errorCode);
    }

    public NoSuchAccommodationType(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public NoSuchAccommodationType(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public NoSuchAccommodationType(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}

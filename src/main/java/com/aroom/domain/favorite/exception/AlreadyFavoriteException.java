package com.aroom.domain.favorite.exception;

import com.aroom.global.error.ErrorCode;
import com.aroom.global.error.ServiceException;

public class AlreadyFavoriteException extends ServiceException {

    private static final ErrorCode errorCode = ErrorCode.ALREADY_FAVORITE;
    public AlreadyFavoriteException(String message) {
        super(message, errorCode);
    }
}

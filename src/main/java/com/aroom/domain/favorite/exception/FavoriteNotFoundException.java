package com.aroom.domain.favorite.exception;

import com.aroom.global.error.ErrorCode;
import com.aroom.global.error.ServiceException;

public class FavoriteNotFoundException extends ServiceException {

    private static final ErrorCode errorCode = ErrorCode.FAVORITE_NOT_FOUND;

    public FavoriteNotFoundException(String message) {
        super(message, errorCode);
    }
}

package com.fastcampus.mini9.common.response;

import lombok.Getter;

@Getter
public class ErrorResponseBody {

    private final boolean isSuccessful;
    private final Integer statusCode;
    private final String message;

    public ErrorResponseBody(boolean isSuccessful, Integer statusCode, String message) {
        this.isSuccessful = isSuccessful;
        this.statusCode = statusCode;
        this.message = message;
    }

    public static ErrorResponseBody unsuccessful(String message) {
        return new ErrorResponseBody(false, 400, message);
    }

    public static ErrorResponseBody unsuccessful(Integer statusCode, String message) {
        return new ErrorResponseBody(false, statusCode, message);
    }
}

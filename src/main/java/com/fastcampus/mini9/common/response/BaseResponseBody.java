package com.fastcampus.mini9.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BaseResponseBody {

    protected static final String SUCCESS_MSG = "success";
    protected static final String FAIL_MSG = "fail";

    private final Integer statusCode;
    private final String message;

    public BaseResponseBody(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static BaseResponseBody success() {
        return new BaseResponseBody(HttpStatus.OK.value(), SUCCESS_MSG);
    }

    public static BaseResponseBody success(String msg) {
        return new BaseResponseBody(HttpStatus.OK.value(), msg);
    }

    public static BaseResponseBody fail() {
        return new BaseResponseBody(HttpStatus.BAD_REQUEST.value(), FAIL_MSG);
    }

    public static BaseResponseBody fail(String msg) {
        return new BaseResponseBody(HttpStatus.BAD_REQUEST.value(), msg);
    }
}

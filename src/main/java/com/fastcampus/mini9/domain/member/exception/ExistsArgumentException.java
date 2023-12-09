package com.fastcampus.mini9.domain.member.exception;

import com.fastcampus.mini9.common.exception.BaseException;
import com.fastcampus.mini9.common.exception.ErrorCode;

public class ExistsArgumentException extends BaseException {

    public ExistsArgumentException() {
        super(ErrorCode.ExistsArgument.getMsg(), ErrorCode.ExistsArgument);
    }

    public ExistsArgumentException(String message) {
        super(message, ErrorCode.ExistsArgument);
    }
}

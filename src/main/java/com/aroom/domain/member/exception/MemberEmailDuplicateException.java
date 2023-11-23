package com.aroom.domain.member.exception;

import com.aroom.global.error.ErrorCode;
import com.aroom.global.error.ServiceException;

public class MemberEmailDuplicateException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.DUPLICATE_EMAIL;

    public MemberEmailDuplicateException() {
        super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
    }
}

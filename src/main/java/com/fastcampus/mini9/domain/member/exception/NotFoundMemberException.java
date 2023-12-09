package com.fastcampus.mini9.domain.member.exception;

import com.fastcampus.mini9.common.exception.BaseException;
import com.fastcampus.mini9.common.exception.ErrorCode;

public class NotFoundMemberException extends BaseException {

    public NotFoundMemberException() {
        super(ErrorCode.NotFoundMember.getMsg(), ErrorCode.NotFoundMember);
    }

    public NotFoundMemberException(String message) {
        super(message, ErrorCode.NotFoundMember);
    }
}

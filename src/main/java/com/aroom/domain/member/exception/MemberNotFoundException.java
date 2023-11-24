package com.aroom.domain.member.exception;

import com.aroom.global.error.ErrorCode;
import com.aroom.global.error.ServiceException;

public class MemberNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_NOT_FOUND;

	public MemberNotFoundException() {
		super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
	}
}

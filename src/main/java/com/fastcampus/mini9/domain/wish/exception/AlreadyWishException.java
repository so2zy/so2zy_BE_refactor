package com.fastcampus.mini9.domain.wish.exception;

import com.fastcampus.mini9.common.exception.BaseException;
import com.fastcampus.mini9.common.exception.ErrorCode;

public class AlreadyWishException extends BaseException {

	public AlreadyWishException() {
		super(ErrorCode.AlreadyWish);
	}

	public AlreadyWishException(String message) {
		super(message, ErrorCode.NoExistWish);
	}
}


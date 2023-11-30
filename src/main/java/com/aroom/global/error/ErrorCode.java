package com.aroom.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	// 회원
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일계정입니다."),

	// 찜하기
	FAVORITE_NOT_FOUND(HttpStatus.BAD_REQUEST, "이미 삭제된 찜 상품입니다."),
	ALREADY_FAVORITE(HttpStatus.BAD_REQUEST, "이미 찜한 상태입니다."),

	//사용자 권한
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
	INVALID_AUTH(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다."),
	;

	private final HttpStatus httpStatus;
	private final String simpleMessage;

	ErrorCode(HttpStatus httpStatus, String simpleMessage) {
		this.httpStatus = httpStatus;
		this.simpleMessage = simpleMessage;
	}
}

package com.fastcampus.mini9.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NoSuchElement(400, "noSuchElement"),
    NoSuchProduct(400, "noSuchProduct"),
    NoSuchCategory(400, "noSuchCategory"),
    AlreadyExistWish(400, "alreadyExistWish"),
    NoSuchWish(400, "noSuchWish"),
    NoPermission(401, "noPermission"),
    ImageUploadError(400, "이미지 업로드 중 오류 발생"),
    ImageDeleteError(400, "이미지 삭제 중 오류 발생"),
    InvalidImageType(400, "유효하지 않은 이미지 형식입니다"),
    ExistsArgument(400, "이미 존재합니다"),
    NotFoundMember(400, "회원을 찾을 수 없습니다"),
    AlreadyWish(400, "위시리스트에 이미 등록되었습니다."),
	  NoExistWish(400, "위시리스트에 등록되지 않은 숙소입니다.");
    ;

	private final Integer code;
	private final String msg;

	ErrorCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}

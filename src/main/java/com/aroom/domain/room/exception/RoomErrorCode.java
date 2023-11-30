package com.aroom.domain.room.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomErrorCode {
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 방이 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    RoomErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}

package com.aroom.domain.room.exception;

import lombok.Getter;

@Getter
public class RoomNotFoundException extends RuntimeException{
    private final RoomErrorCode errorCode;

    public RoomNotFoundException(String message, RoomErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}

package com.aroom.domain.roomCart.exception;

public class RoomCartNotFoundException extends RuntimeException {

    public RoomCartNotFoundException() {
        super("Room Cart가 존재하지 않습니다.");
    }
}


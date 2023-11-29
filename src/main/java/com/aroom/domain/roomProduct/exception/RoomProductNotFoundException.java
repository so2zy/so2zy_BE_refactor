package com.aroom.domain.roomProduct.exception;

public class RoomProductNotFoundException extends RuntimeException {

    public RoomProductNotFoundException() {
        super("Room Product가 존재하지 않습니다.");
    }
}

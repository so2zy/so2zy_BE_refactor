package com.aroom.domain.roomCart.exception;

public class WrongDateException extends RuntimeException{

    public WrongDateException() {
        super("시작일/종료일을 다시 확인해주세요.");
    }
}

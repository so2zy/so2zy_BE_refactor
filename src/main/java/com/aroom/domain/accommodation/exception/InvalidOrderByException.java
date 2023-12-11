package com.aroom.domain.accommodation.exception;

public class InvalidOrderByException extends RuntimeException{

    public InvalidOrderByException() {
        super("유효하지 않은 정렬조건입니다. asc 혹은 desc를 입력하세요");
    }
}

package com.aroom.domain.accommodation.exception;

public class AccommodationNotFoundException extends RuntimeException {

    public AccommodationNotFoundException() {
        super("숙소를 찾을 수 없습니다.");
    }
}

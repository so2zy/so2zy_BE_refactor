package com.aroom.domain.reservation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationErrorCode {
    OUT_OF_STOCK_ERROR(HttpStatus.NOT_FOUND, "방이 품절 되었습니다."),
    MAXIMUM_CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "예약 최대 인원을 초과합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String simpleMessage;

    ReservationErrorCode(HttpStatus httpStatus, String simpleMessage) {
        this.httpStatus = httpStatus;
        this.simpleMessage = simpleMessage;
    }
}

package com.aroom.domain.reservation.exception;

public class MaximumCapacityExceededException extends RuntimeException {
    private final ReservationErrorCode errorCode;

    public MaximumCapacityExceededException(String message, ReservationErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

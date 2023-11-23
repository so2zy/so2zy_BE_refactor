package com.aroom.domain.reservation.exception;

import com.aroom.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class OutOfStockException extends RuntimeException{
    private final ReservationErrorCode errorCode;

    public OutOfStockException(String message, ReservationErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}

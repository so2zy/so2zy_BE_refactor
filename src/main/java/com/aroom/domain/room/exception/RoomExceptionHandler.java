package com.aroom.domain.room.exception;

import com.aroom.domain.reservation.exception.OutOfStockException;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(RoomNotFoundException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
            .body(new ApiResponse<>(LocalDateTime.now(), ex.getErrorCode().getSimpleMessage(), null));
    }
}

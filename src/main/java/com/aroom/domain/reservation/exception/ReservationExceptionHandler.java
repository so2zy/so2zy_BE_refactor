package com.aroom.domain.reservation.exception;

import com.aroom.global.error.ServiceException;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(OutOfStockException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
            .body(new ApiResponse<>(LocalDateTime.now(), ex.getErrorCode().getSimpleMessage(), null));
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(MaximumCapacityExceededException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
            .body(new ApiResponse<>(LocalDateTime.now(), ex.getErrorCode().getSimpleMessage(), null));
    }
}

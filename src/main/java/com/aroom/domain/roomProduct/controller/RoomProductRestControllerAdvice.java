package com.aroom.domain.roomProduct.controller;

import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.domain.roomProduct.exception.RoomProductNotFoundException;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class RoomProductRestControllerAdvice {

    @ExceptionHandler(RoomProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> RoomProductNotFoundException(
        RoomProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }
}

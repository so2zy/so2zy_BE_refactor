package com.aroom.domain.roomCart.controller;

import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomCartRestControllerAdivce {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ApiResponse<Void>> OutOfStockException(
        OutOfStockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }

}

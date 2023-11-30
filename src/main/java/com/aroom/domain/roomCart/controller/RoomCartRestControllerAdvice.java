package com.aroom.domain.roomCart.controller;

import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.domain.roomCart.exception.RoomCartNotFoundException;
import com.aroom.domain.roomCart.exception.WrongDateException;
import com.aroom.domain.roomProduct.exception.RoomProductNotFoundException;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomCartRestControllerAdvice {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ApiResponse<Void>> OutOfStockException(
        OutOfStockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }

    @ExceptionHandler(RoomCartNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> RoomCartNotFoundException(
        RoomCartNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }

    @ExceptionHandler(RoomProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> RoomProductNotFoundException(
        RoomProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }

    @ExceptionHandler(WrongDateException.class)
    public ResponseEntity<ApiResponse<Void>> WrongDateException(
        WrongDateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }
}

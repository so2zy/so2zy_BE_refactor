package com.aroom.domain.accommodation.controller;

import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.roomCart.exception.WrongDateException;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccommodationRestControllerAdvice {

    @ExceptionHandler(AccommodationNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> accommodationNotFoundException(
        AccommodationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }

    @ExceptionHandler(WrongDateException.class)
    public ResponseEntity<ApiResponse<Void>> WrongDateException(
        WrongDateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }
}


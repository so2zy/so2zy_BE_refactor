package com.aroom.domain.cart.controller;

import com.aroom.domain.cart.exception.CartNotFoundException;
import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class CartRestControllerAdvice {

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> CartNotFoundException(
        OutOfStockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), null));
    }

}

package com.aroom.domain.cart.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException() {
        super("Cart가 존재하지 않습니다.");
    }
}


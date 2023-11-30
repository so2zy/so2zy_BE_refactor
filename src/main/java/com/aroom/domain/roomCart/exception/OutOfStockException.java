package com.aroom.domain.roomCart.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException() {
        super("상품의 재고 부족으로 장바구니 담기가 불가합니다.");
    }
}

package com.aroom.domain.cart.controller;


import com.aroom.domain.cart.dto.response.FindCartResponse;
import com.aroom.domain.cart.service.CartService;
import com.aroom.global.resolver.Login;
import com.aroom.global.resolver.LoginInfo;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
public class CartRestController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<FindCartResponse>> findCartList(@Login LoginInfo loginInfo){
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(LocalDateTime.now(), "장바구니 조회에 성공했습니다.",
                cartService.getCartList(loginInfo.memberId())));
    }
}

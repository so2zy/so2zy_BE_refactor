package com.aroom.domain.roomCart.controller;

import com.aroom.domain.roomCart.dto.response.FindCartResponse;
import com.aroom.domain.roomCart.dto.request.RoomCartRequest;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.service.RoomCartService;
import com.aroom.global.resolver.Login;
import com.aroom.global.resolver.LoginInfo;
import com.aroom.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/carts")
public class RoomCartRestController {

    private final RoomCartService roomCartService;

    @PostMapping("/{room_id}")
    public ResponseEntity<ApiResponse<RoomCartResponse>> postRoomCart(
        @Login LoginInfo loginInfo,
        @PathVariable long room_id, @RequestBody @Valid RoomCartRequest roomCartRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(LocalDateTime.now(), "성공적으로 장바구니에 등록했습니다.",
                roomCartService.postRoomCart(loginInfo.memberId(), room_id, roomCartRequest)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FindCartResponse>> findCartList(@Login LoginInfo loginInfo){
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(LocalDateTime.now(), "장바구니 조회에 성공했습니다.",
                roomCartService.getCartList(loginInfo.memberId())));
    }
}

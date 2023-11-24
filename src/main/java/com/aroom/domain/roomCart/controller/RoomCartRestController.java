package com.aroom.domain.roomCart.controller;

import com.aroom.domain.roomCart.dto.response.RoomCartResponseDTO;
import com.aroom.domain.roomCart.service.RoomCartService;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
public class RoomCartRestController {

    private final RoomCartService roomCartService;

    @PostMapping("/{member_id}/{room_id}")
    public ResponseEntity<ApiResponse<RoomCartResponseDTO>> postRoomCart(
        @PathVariable long member_id,
        @PathVariable long room_id) { //member_id를 이제 jwt로부터 가져올 것
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse(LocalDateTime.now(), "성공적으로 장바구니에 등록했습니다.",
                roomCartService.postRoomCart(member_id, room_id)));
    }
}

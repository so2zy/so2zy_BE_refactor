package com.aroom.domain.room.controller;

import com.aroom.domain.room.dto.response.RoomResponseDTO;
import com.aroom.domain.room.service.RoomService;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/rooms")
public class RoomRestController {

    private final RoomService roomService;

    @GetMapping("/{accommodation_id}")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> getRoom(
        @PathVariable long accommodation_id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse(LocalDateTime.now(), "객실 상세 정보를 성공적으로 조회했습니다.",
                roomService.getRoom(accommodation_id)));
    }
}

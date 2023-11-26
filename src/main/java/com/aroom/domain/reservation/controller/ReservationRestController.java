package com.aroom.domain.reservation.controller;

import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.dto.response.ReservationResponse;
import com.aroom.domain.reservation.service.ReservationService;
import com.aroom.global.resolver.Login;
import com.aroom.global.resolver.LoginInfo;
import com.aroom.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reservations")
public class ReservationRestController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> reservationRoom(
        @RequestBody @Valid ReservationRequest request,
        @Login LoginInfo loginInfo) {

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(LocalDateTime.now(),
            "객실 예약에 성공했습니다.",
            reservationService.reserveRoom(request, loginInfo.memberId())));
    }
}

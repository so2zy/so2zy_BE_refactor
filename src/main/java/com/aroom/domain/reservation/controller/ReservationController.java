package com.aroom.domain.reservation.controller;

import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.dto.response.ReservedReservationResponse;
import com.aroom.domain.reservation.service.ReservationService;
import com.aroom.global.response.ApiResponse;
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
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservedReservationResponse>> reservationRoom(@RequestBody ReservationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}

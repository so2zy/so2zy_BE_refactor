package com.aroom.util.fixture;

import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.dto.response.ReservationResponse;
import com.aroom.domain.room.dto.request.RoomReservationRequest;
import com.aroom.domain.room.dto.response.RoomReservationResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public abstract class ReservationFixture {

    public static ReservationRequest getReservationRequest(List<RoomReservationRequest> roomReservationRequests) {
        return ReservationRequest.builder()
            .roomList(roomReservationRequests)
            .agreement(true)
            .isFromCart(true)
            .build();
    }

    public static ReservationResponse getReservationResponse(List<RoomReservationResponse> roomReservationResponses) {
        return ReservationResponse.builder()
            .roomList(roomReservationResponses)
            .reservationNumber(3L)
            .dealDateTime(LocalDateTime.now())
            .build();
    }

    public static RoomReservationRequest getRoomReservationRequest() {
        return RoomReservationRequest.builder()
            .roomId(1L)
            .startDate(LocalDate.parse("2023-12-22"))
            .endDate(LocalDate.parse("2023-12-23"))
            .price(100000)
            .personnel(2)
            .build();
    }

    public static RoomReservationResponse getRoomReservationResponse() {
        return RoomReservationResponse.builder()
            .roomId(1L)
            .type("패밀리")
            .checkIn(LocalTime.of(19, 0))
            .checkOut(LocalTime.of(13, 0))
            .capacity(2)
            .maxCapacity(4)
            .price(100000)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .roomImageUrl("testimage.com")
            .roomReservationNumber(1L)
            .build();
    }
}

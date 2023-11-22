package com.aroom.domain.reservation.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationResponse {
    private Long roomId;
    private String roomType;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private int capacity;
    private int maxCapacity;
    private Long roomReservationNumber;
    private Long reservationNumber;
    private LocalDateTime dealDateTime;

    @Builder
    public ReservationResponse(Long roomId, String roomType, LocalTime checkIn,
        LocalTime checkOut, int capacity, int maxCapacity, Long roomReservationNumber,
        Long reservationNumber, LocalDateTime dealDateTime) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.roomReservationNumber = roomReservationNumber;
        this.reservationNumber = reservationNumber;
        this.dealDateTime = dealDateTime;
    }

}

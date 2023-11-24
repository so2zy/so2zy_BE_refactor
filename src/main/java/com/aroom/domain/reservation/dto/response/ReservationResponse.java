package com.aroom.domain.reservation.dto.response;

import com.aroom.domain.room.dto.response.RoomReservationResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationResponse {

    private List<RoomReservationResponse> roomList = new ArrayList<>();
    private Long roomReservationNumber;
    private Long reservationNumber;
    private LocalDateTime dealDateTime;

    @Builder
    public ReservationResponse(List<RoomReservationResponse> roomList, Long roomReservationNumber,
        Long reservationNumber, LocalDateTime dealDateTime) {
        this.roomList = roomList;
        this.roomReservationNumber = roomReservationNumber;
        this.reservationNumber = reservationNumber;
        this.dealDateTime = dealDateTime;
    }
}

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
    private Long reservationNumber;
    private LocalDateTime dealDateTime;

    @Builder
    public ReservationResponse(List<RoomReservationResponse> roomList, Long reservationNumber,
        LocalDateTime dealDateTime) {
        this.roomList = roomList;
        this.reservationNumber = reservationNumber;
        this.dealDateTime = dealDateTime;
    }
}

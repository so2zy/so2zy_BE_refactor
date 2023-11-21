package com.aroom.domain.reservation.dto.request;

import com.aroom.domain.room.dto.request.ReservationRoomRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ReservationRequest {

    private List<ReservationRoomRequest> roomList = new ArrayList<>();
    private int personnel;
    private boolean agreement;

    @Builder
    public ReservationRequest(List<ReservationRoomRequest> roomList, int personnel, boolean agreement) {
        this.roomList = roomList;
        this.personnel = personnel;
        this.agreement = agreement;
    }
}

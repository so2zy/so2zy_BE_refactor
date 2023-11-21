package com.aroom.domain.reservation.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ReservationRequest {

    private List<Long> roomIdList = new ArrayList<>();
    private int personnel;

    @Builder
    public ReservationRequest(List<Long> roomIdList, int personnel) {
        this.roomIdList = roomIdList;
        this.personnel = personnel;
    }
}

package com.aroom.domain.reservation.dto.request;

import com.aroom.domain.room.dto.request.RoomReservationRequest;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ReservationRequest {

    @NotNull(message = "방을 하나 이상 예약 해야 합니다.")
    private List<RoomReservationRequest> roomList;

    @NotNull(message = "약관에 대한 동의 여부가 존재하지 않습니다.")
    private Boolean agreement;

    @NotNull(message = "카트 체크 여부가 필요합니다.")
    private Boolean isFromCart;

    @Builder
    public ReservationRequest(List<RoomReservationRequest> roomList, Boolean agreement,
        Boolean isFromCart) {
        this.roomList = roomList;
        this.agreement = agreement;
        this.isFromCart = isFromCart;
    }

    public boolean isFromCart() {
        return isFromCart;
    }
}

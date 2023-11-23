package com.aroom.domain.reservation.dto.request;

import com.aroom.domain.room.dto.request.ReservationRoomRequest;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ReservationRequest {

    @NotNull(message = "방을 하나 이상 예약 해야 합니다.")
    private List<ReservationRoomRequest> roomList;

    @Min(value = 1, message = "예약 인원은 한 명 이상 이어야 합니다.")
    private int personnel;

    @NotNull(message = "약관에 대한 동의 여부가 존재하지 않습니다.")
    private Boolean agreement;

    @Builder
    public ReservationRequest(List<ReservationRoomRequest> roomList, int personnel, boolean agreement) {
        this.roomList = roomList;
        this.personnel = personnel;
        this.agreement = agreement;
    }
}

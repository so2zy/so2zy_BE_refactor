package com.aroom.domain.room.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomReservationRequest {
    private Long roomId;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    private int price;

    @Min(value = 1, message = "예약 인원은 한 명 이상 이어야 합니다.")
    private int personnel;

    @Builder
    public RoomReservationRequest(Long roomId, LocalDate startDate, LocalDate endDate, int price, int personnel) {
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.personnel = personnel;
    }
}

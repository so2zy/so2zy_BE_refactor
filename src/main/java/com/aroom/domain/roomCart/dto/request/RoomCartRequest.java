package com.aroom.domain.roomCart.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomCartRequest {

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    @NotNull
    private int personnel;

    @Builder
    public RoomCartRequest(LocalDate startDate, LocalDate endDate, int personnel) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.personnel = personnel;
    }
}

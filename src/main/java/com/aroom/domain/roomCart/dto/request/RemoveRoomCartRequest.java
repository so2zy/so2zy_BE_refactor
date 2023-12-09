package com.aroom.domain.roomCart.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RemoveRoomCartRequest {
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
}

package com.aroom.domain.room.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CartRoomResponse {
    private Long roomId;
    private String type;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private int capacity;
    private int maxCapacity;
    private int price;
    private LocalDate startDate;
    private LocalDate endDate;
    private String roomImageUrl;
    private int personnel;

    @Builder
    public CartRoomResponse(Long roomId, String type, LocalTime checkIn, LocalTime checkOut,
        int capacity, int maxCapacity, int price, LocalDate startDate, LocalDate endDate,
        String roomImageUrl, int personnel) {
        this.roomId = roomId;
        this.type = type;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomImageUrl = roomImageUrl;
        this.personnel = personnel;
    }
}

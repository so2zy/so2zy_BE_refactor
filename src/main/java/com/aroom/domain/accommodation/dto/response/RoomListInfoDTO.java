package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.room.model.Room;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomListInfoDTO {

    private Long id;
    private String type;
    private int price;
    private int capacity;
    private int maxCapacity;
    private String checkIn;
    private String checkOut;
    private int stock;
    private String url;

    @Builder
    public RoomListInfoDTO(Long id, String type, int price, int capacity, int maxCapacity,
        String checkIn, String checkOut, int stock, String url) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.stock = stock;
        this.url = url;
    }

    public RoomListInfoDTO(Room room) {
        this.id = room.getId();
        this.type = room.getType();
        this.price = room.getPrice();
        this.capacity = room.getCapacity();
        this.maxCapacity = room.getMaxCapacity();
        this.checkIn = room.getCheckIn().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.checkOut = room.getCheckOut().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.stock = room.getStock();
        this.url = room.getRoomImageList().get(0).getUrl();
    }
}

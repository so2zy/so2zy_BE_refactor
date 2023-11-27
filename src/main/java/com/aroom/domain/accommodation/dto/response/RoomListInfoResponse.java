package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomListInfoResponse {

    private Long id;
    private String type;
    private int price;
    private int capacity;
    private int maxCapacity;
    private String checkIn;
    private String checkOut;
    private int soldCount;
    private String url;

    @Builder
    public RoomListInfoResponse(Long id, String type, int price, int capacity, int maxCapacity,
        String checkIn, String checkOut, int soldCount, String url) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.soldCount = soldCount;
        this.url = url;
    }

    public RoomListInfoResponse(Room room) {
        this.id = room.getId();
        this.type = room.getType();
        this.price = room.getPrice();
        this.capacity = room.getCapacity();
        this.maxCapacity = room.getMaxCapacity();
        this.checkIn = room.getCheckIn().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.checkOut = room.getCheckOut().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.soldCount = room.getSoldCount();
        this.url = room.getRoomImageList().stream()
            .map(RoomImage::getUrl)
            .findFirst()
            .orElse(null);
    }
}

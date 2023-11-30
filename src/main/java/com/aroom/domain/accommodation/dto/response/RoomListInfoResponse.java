package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomListInfoResponse {

    private Long id;
    private String type;
    private int price;
    private int capacity;
    private int maxCapacity;
    private String checkIn;
    private String checkOut;
    private String url;
    private int stock;


    @Builder
    public RoomListInfoResponse(Long id, String type, int price, int capacity, int maxCapacity,
        String checkIn, String checkOut, String url, int stock) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.url = url;
        this.stock = stock;
    }


    public RoomListInfoResponse(Room room, Long betweenDays, LocalDate startDate) {
        int minStock = Integer.MAX_VALUE;
        for(RoomProduct roomProduct : room.getRoomProductList()){
            minStock = Math.min(roomProduct.getStock(),minStock);
        }
        this.id = room.getId();
        this.type = room.getType();
        this.price = (int) (room.getPrice() * betweenDays);
        this.capacity = room.getCapacity();
        this.maxCapacity = room.getMaxCapacity();
        this.checkIn = room.getCheckIn().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.checkOut = room.getCheckOut().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.url = room.getRoomImageList().stream()
            .map(RoomImage::getUrl)
            .findFirst()
            .orElse(null);
        this.stock = minStock;
    }
}

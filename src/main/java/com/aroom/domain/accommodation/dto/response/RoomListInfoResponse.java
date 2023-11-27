package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    public RoomListInfoResponse(Room room, String startDate, Integer personnel) {
        this.id = room.getId();
        this.type = room.getType();
        this.price = room.getPrice();
        this.capacity = room.getCapacity();
        this.maxCapacity = room.getMaxCapacity();
        this.checkIn = room.getCheckIn().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.checkOut = room.getCheckOut().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.url = room.getRoomImageList().stream()
            .map(RoomImage::getUrl)
            .findFirst()
            .orElse(null);
        for(RoomProduct roomProduct : room.getRoomProductList()) {
            if(roomProduct.getStartDate().equals(startDate)) {
                this.stock = roomProduct.getStock();
            }
        }
    }
}

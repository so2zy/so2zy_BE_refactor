package com.aroom.domain.roomCart.dto.response;

import com.aroom.domain.roomCart.model.RoomCart;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomCartInfoDTO {

    private long room_id;
    private long cart_id;

    @Builder
    public RoomCartInfoDTO(long room_id, long cart_id) {
        this.room_id = room_id;
        this.cart_id = cart_id;
    }

    public RoomCartInfoDTO(RoomCart roomCart) {
        this.room_id = roomCart.getRoom().getId();
        this.cart_id = roomCart.getCart().getId();
    }
}

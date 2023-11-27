package com.aroom.domain.roomCart.dto.response;

import com.aroom.domain.roomCart.model.RoomCart;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomCartInfoResponse {

    private long room_id;
    private long cart_id;

    @Builder
    public RoomCartInfoResponse(long room_id, long cart_id) {
        this.room_id = room_id;
        this.cart_id = cart_id;
    }

    public RoomCartInfoResponse(RoomCart roomCart) {
        this.room_id = roomCart.getRoomProduct().getRoom().getId();
        this.cart_id = roomCart.getCart().getId();
    }
}

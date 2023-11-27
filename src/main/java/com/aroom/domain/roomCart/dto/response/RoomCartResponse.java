package com.aroom.domain.roomCart.dto.response;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.roomCart.model.RoomCart;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomCartResponse {

    private List<RoomCartInfoResponse> roomCartList;

    @Builder
    public RoomCartResponse(List<RoomCartInfoResponse> roomCartList) {
        this.roomCartList = roomCartList;
    }


    public RoomCartResponse(Cart cart) {
        List<RoomCartInfoResponse> roomCartInfoResponseList = new ArrayList<>();
        for (RoomCart roomCart : cart.getRoomCartList()) {
            RoomCartInfoResponse roomCartInfoResponse = new RoomCartInfoResponse(roomCart);
            roomCartInfoResponseList.add(roomCartInfoResponse);
        }
        this.roomCartList = roomCartInfoResponseList;
    }
}

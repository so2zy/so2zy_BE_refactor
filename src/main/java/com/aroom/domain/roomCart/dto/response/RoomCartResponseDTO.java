package com.aroom.domain.roomCart.dto.response;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.roomCart.model.RoomCart;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoomCartResponseDTO {

    private long cart_id;
    private List<RoomCartInfoDTO> roomCartList;

    public RoomCartResponseDTO(Cart cart) {
        this.cart_id = cart.getId();
        List<RoomCartInfoDTO> roomCartInfoDTOList = new ArrayList<>();
        for (RoomCart roomCart : cart.getRoomCartList()) {
            RoomCartInfoDTO roomCartInfoDTO = new RoomCartInfoDTO(roomCart);
            roomCartInfoDTOList.add(roomCartInfoDTO);
        }
        this.roomCartList = roomCartInfoDTOList;
    }
}

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
public class RoomCartResponseDTO {

    private List<RoomCartInfoDTO> roomCartList;

    @Builder
    public RoomCartResponseDTO(List<RoomCartInfoDTO> roomCartList) {
        this.roomCartList = roomCartList;
    }


    public RoomCartResponseDTO(Cart cart) {
        List<RoomCartInfoDTO> roomCartInfoDTOList = new ArrayList<>();
        for (RoomCart roomCart : cart.getRoomCartList()) {
            RoomCartInfoDTO roomCartInfoDTO = new RoomCartInfoDTO(roomCart);
            roomCartInfoDTOList.add(roomCartInfoDTO);
        }
        this.roomCartList = roomCartInfoDTOList;
    }
}

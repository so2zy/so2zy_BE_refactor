package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.room.dto.response.CartRoomResponse;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CartAccommodationResponse {
    private Long accommodationId;
    private String accommodationName;
    private String address;
    private List<CartRoomResponse> roomList = new ArrayList<>();

    @Builder
    public CartAccommodationResponse(Long accommodationId, String accommodationName, String address,
        List<CartRoomResponse> roomList) {
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.address = address;
        this.roomList = roomList;
    }
}

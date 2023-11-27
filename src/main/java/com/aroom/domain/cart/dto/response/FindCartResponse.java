package com.aroom.domain.cart.dto.response;

import com.aroom.domain.accommodation.dto.response.CartAccommodationResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

public class FindCartResponse {

    private List<CartAccommodationResponse> accommodationList = new ArrayList<>();

    @Builder
    public FindCartResponse(List<CartAccommodationResponse> accommodationList) {
        this.accommodationList = accommodationList;
    }
}

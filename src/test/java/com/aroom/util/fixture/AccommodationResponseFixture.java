package com.aroom.util.fixture;

import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.dto.response.RoomListInfoResponse;
import java.util.List;

public abstract class AccommodationResponseFixture {

    public static AccommodationResponse getAccommodationDetailFixture(
        List<RoomListInfoResponse> roomResponse) {
        return AccommodationResponse.builder()
            .id(1L)
            .accommodationName("롯데호텔")
            .latitude(150.54f)
            .longitude(100.5f)
            .address("서울특별시 중구 을지로 30")
            .phoneNumber("02-771-1000")
            .roomInfoList(roomResponse)
            .accommodationUrl(
                "https://www.lottehotel.com/content/dam/lotte-hotel/lotte/seoul/dining/restaurant/pierre-gagnaire/180711-33-2000-din-seoul-hotel.jpg.thumb.768.768.jpg")
            .build();
    }

    public static List<RoomListInfoResponse> getRoomListResponse() {
        return List.of(RoomListInfoResponse.builder()
            .id(1L)
            .type("DELUXE")
            .price(350000)
            .capacity(2)
            .maxCapacity(4)
            .checkIn("15:00")
            .checkOut("11:00")
            .url("naver.com")
            .build());
    }
}

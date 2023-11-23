package com.aroom.domain.accommodation.dto;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.aroom.domain.room.model.Room;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationListResponse {
    private Long id;

    private String name;

    private float latitude;

    private float longitude;

    private String addressCode;

    private int likeCount;

    private String phoneNumber;


    public static AccommodationListResponse fromEntity(Accommodation accommodation){

        //accommodation.getRoomList에서 스트림으로 roomDto 타입으로 변환 후 넣어줘야함

        return AccommodationListResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .latitude(accommodation.getLatitude())
            .longitude(accommodation.getLongitude())
            .addressCode(accommodation.getAddressCode())
            .likeCount(accommodation.getLikeCount())
            .phoneNumber(accommodation.getPhoneNumber())

            .build();


    }
}

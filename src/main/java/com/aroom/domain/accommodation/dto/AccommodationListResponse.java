package com.aroom.domain.accommodation.dto;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.room.model.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

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

    private String accommodationImageUrl;

    private Integer page;

    private Integer size;
    //객실의 최저가를 숙소 조회할때 대표 가격으로 출력합니다.
    private Integer price;

    public static AccommodationListResponse fromEntity(Accommodation accommodation, Integer page, Integer size) {


        String imageUrl = accommodation.getAccommodationImageList()
            .stream()
            .map(AccommodationImageList::fromEntity)
            .map(AccommodationImageList::getUrl)
            .findFirst()
            .orElse(null);
        int minimumPrice = accommodation.getRoomList().stream()
            .mapToInt(Room::getPrice)
            .min()
            .orElse(100000);

        return AccommodationListResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .latitude(accommodation.getLatitude())
            .longitude(accommodation.getLongitude())
            .addressCode(accommodation.getAddressCode())
            .likeCount(accommodation.getLikeCount())
            .phoneNumber(accommodation.getPhoneNumber())
            .accommodationImageUrl(imageUrl)
            .page(page)
            .size(size)
            .price(minimumPrice)
            .build();


    }
}

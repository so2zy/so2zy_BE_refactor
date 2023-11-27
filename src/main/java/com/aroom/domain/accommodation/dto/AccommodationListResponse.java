package com.aroom.domain.accommodation.dto;

import com.aroom.domain.accommodation.model.Accommodation;
import java.util.ArrayList;
import java.util.List;
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

    public static AccommodationListResponse fromEntity(Accommodation accommodation, Integer page, Integer size) {


        String imageUrl = accommodation.getAccommodationImageList()
            .stream()
            .map(AccommodationImageList::fromEntity)
            .map(AccommodationImageList::getUrl)
            .findFirst()
            .orElse(null);


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
            .build();


    }
}

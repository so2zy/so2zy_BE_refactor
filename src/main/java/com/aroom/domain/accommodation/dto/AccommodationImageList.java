package com.aroom.domain.accommodation.dto;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccommodationImageList {

    private Long id;

    private String url;
    public static AccommodationImageList fromEntity(AccommodationImage accommodationImage){
        return AccommodationImageList.builder()
            .id(accommodationImage.getId())
            .url(accommodationImage.getUrl())
            .build();
    }

}

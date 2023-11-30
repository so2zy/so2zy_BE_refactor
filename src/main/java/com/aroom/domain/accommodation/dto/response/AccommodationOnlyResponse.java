package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AccommodationOnlyResponse {

    private Long id;
    private String accommodationName;
    private Float latitude;
    private Float longitude;
    private String address;
    private String phoneNumber;
    private String accommodationUrl;
    private Boolean favorite;

    @Builder
    public AccommodationOnlyResponse(Long id, String accommodationName, Float latitude,
        Float longitude, String addressCode, String address, String phoneNumber,
        String accommodationUrl, Boolean favorite) {
        this.id = id;
        this.accommodationName = accommodationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accommodationUrl = accommodationUrl;
        this.favorite = favorite;
    }

    public AccommodationOnlyResponse(Accommodation accommodation, Boolean isFavorite){
        this.id = accommodation.getId();
        this.accommodationName = accommodation.getName();
        this.latitude = accommodation.getLatitude();
        this.longitude = accommodation.getLongitude();
        this.address = accommodation.getAddress();
        this.phoneNumber = accommodation.getPhoneNumber();
        this.accommodationUrl = accommodation.getAccommodationImageList().stream()
            .map(AccommodationImage::getUrl)
            .findFirst()
            .orElse(null);
        this.favorite = isFavorite;
    }
}

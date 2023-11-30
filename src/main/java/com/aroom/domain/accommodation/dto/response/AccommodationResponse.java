package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AccommodationResponse {

    private Long id;
    private String accommodationName;
    private Float latitude;
    private Float longitude;
    private String address;
    private String phoneNumber;
    private String accommodationUrl;
    private Boolean favorite;
    private List<RoomListInfoResponse> roomInfoList;

    @Builder
    public AccommodationResponse(Long id, String accommodationName, Float latitude,
        Float longitude, String address, String phoneNumber, String accommodationUrl,
        Boolean favorite,
        List<RoomListInfoResponse> roomInfoList) {
        this.id = id;
        this.accommodationName = accommodationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accommodationUrl = accommodationUrl;
        this.favorite = favorite;
        this.roomInfoList = roomInfoList;
    }

    public AccommodationResponse(Accommodation accommodation, Integer personnel, Boolean isFavorite,
        Long betweenDays, LocalDate startDate) {
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
        this.roomInfoList = accommodation.getRoomList().stream()
            .filter(room -> room.getMaxCapacity() >= personnel)
            .filter(room -> room.getRoomProductList().stream()
                .allMatch(rp -> rp.getStock() > 0))
            .map(room -> new RoomListInfoResponse(room, betweenDays, startDate))
            .collect(Collectors.toList());
    }
}

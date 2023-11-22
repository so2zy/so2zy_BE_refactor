package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.aroom.domain.room.model.Room;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccommodationResponseDTO {

    private String accommodationName;
    private Float latitude;
    private Float longitude;
    private String addressCode;
    private String phoneNumber;
    private List<AccommodationImage> accommodationImageList;
    private List<Room> roomList;

    @Builder
    public AccommodationResponseDTO(String accommodationName, Float latitude, Float longitude,
        String addressCode, String phoneNumber, List<Room> roomList,
        List<AccommodationImage> accommodationImageList) {
        this.accommodationName = accommodationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressCode = addressCode;
        this.phoneNumber = phoneNumber;
        this.accommodationImageList = accommodationImageList;
        this.roomList = roomList;
    }

    public AccommodationResponseDTO(Accommodation accommodation) {
        this.accommodationName = accommodation.getName();
        this.latitude = accommodation.getLatitude();
        this.longitude = accommodation.getLongitude();
        this.addressCode = accommodation.getAddressCode();
        this.phoneNumber = accommodation.getPhoneNumber();
        this.accommodationImageList = accommodation.getAccommodationImageList();
        this.roomList = accommodation.getRoomList();
    }
}

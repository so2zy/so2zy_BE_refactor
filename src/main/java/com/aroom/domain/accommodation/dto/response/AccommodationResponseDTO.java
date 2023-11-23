package com.aroom.domain.accommodation.dto.response;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.aroom.domain.room.model.Room;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AccommodationResponseDTO {

    private Long id;
    private String accommodationName;
    private Float latitude;
    private Float longitude;
    private String addressCode;
    private String phoneNumber;
    private List<AccommodationImage> accommodationImageList;
    private List<RoomListInfoDTO> roomInfoList;

    @Builder
    public AccommodationResponseDTO(long id, String accommodationName, Float latitude,
        Float longitude, String addressCode, String phoneNumber,
        List<AccommodationImage> accommodationImageList,
        List<RoomListInfoDTO> roomInfoList) {
        this.id = id;
        this.accommodationName = accommodationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressCode = addressCode;
        this.phoneNumber = phoneNumber;
        this.accommodationImageList = accommodationImageList;
        this.roomInfoList = roomInfoList;
    }

    public AccommodationResponseDTO(Accommodation accommodation) {
        this.id = accommodation.getId();
        this.accommodationName = accommodation.getName();
        this.latitude = accommodation.getLatitude();
        this.longitude = accommodation.getLongitude();
        this.addressCode = accommodation.getAddressCode();
        this.phoneNumber = accommodation.getPhoneNumber();
        this.accommodationImageList = accommodation.getAccommodationImageList();
        List<RoomListInfoDTO> roomInfoList = new ArrayList<>();
        for (Room room : accommodation.getRoomList()) {
            RoomListInfoDTO roomListInfoDTO = new RoomListInfoDTO(room);
            roomInfoList.add(roomListInfoDTO);
        }
        this.roomInfoList = roomInfoList;
    }
}

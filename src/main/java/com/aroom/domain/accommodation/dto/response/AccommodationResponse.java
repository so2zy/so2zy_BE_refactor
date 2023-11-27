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
public class AccommodationResponse {

    private Long id;
    private String accommodationName;
    private Float latitude;
    private Float longitude;
    private String addressCode;
    private String phoneNumber;
    private String accommodationUrl;
    private List<RoomListInfoResponse> roomInfoList;

    @Builder
    public AccommodationResponse(Long id, String accommodationName, Float latitude,
        Float longitude, String addressCode, String phoneNumber, String accommodationUrl,
        List<RoomListInfoResponse> roomInfoList) {
        this.id = id;
        this.accommodationName = accommodationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressCode = addressCode;
        this.phoneNumber = phoneNumber;
        this.accommodationUrl = accommodationUrl;
        this.roomInfoList = roomInfoList;
    }

    public AccommodationResponse(Accommodation accommodation, String startDate, Integer personnel, Long memberId) {
        this.id = accommodation.getId();
        this.accommodationName = accommodation.getName();
        this.latitude = accommodation.getLatitude();
        this.longitude = accommodation.getLongitude();
        this.addressCode = accommodation.getAddressCode();
        this.phoneNumber = accommodation.getPhoneNumber();
        this.accommodationUrl = accommodation.getAccommodationImageList().stream()
            .map(AccommodationImage::getUrl)
            .findFirst()
            .orElse(null);
        List<RoomListInfoResponse> roomInfoList = new ArrayList<>();
        for (Room room : accommodation.getRoomList()) {
            RoomListInfoResponse roomListInfoResponse = new RoomListInfoResponse(room, startDate, personnel);
            roomInfoList.add(roomListInfoResponse);
        }
        this.roomInfoList = roomInfoList;
    }
}

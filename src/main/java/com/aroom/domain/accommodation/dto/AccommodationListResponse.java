package com.aroom.domain.accommodation.dto;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.room.model.Room;
import com.querydsl.core.annotations.QueryProjection;
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

    private Integer page;
    private Integer size;
    private Integer totalPage;

    public void setPaginationInfo(Integer page, Integer size, Integer totalPage) {
        this.page = page;
        this.size = size;
        this.totalPage = totalPage;
        //this.totalPage = totalPage;
    }

    private List<InnerClass> body = new ArrayList<>();

    public void addBody(InnerClass innerClass) {
        innerClass.hasValidPrice();
        this.body.add(innerClass);
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class InnerClass {

        private Long id;

        private String name;

        private float latitude;

        private float longitude;

        private String address;

        private int likeCount;

        private String phoneNumber;

        private String accommodationImageUrl;

        private Boolean isAvailable = true;


        private void hasValidPrice(){
            if (price == 0 || price == null){
                isAvailable = false;
            }
        }

        //객실의 최저가를 숙소 조회할때 대표 가격으로 출력합니다.
        private Integer price ;

        @QueryProjection
        public InnerClass(Long id, String name, float latitude, float longitude, String address,
            int likeCount, String phoneNumber, String accommodationImageUrl, Integer price) {
            this.id = id;
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
            this.likeCount = likeCount;
            this.phoneNumber = phoneNumber;
            this.accommodationImageUrl = accommodationImageUrl;
            this.price = price;
        }

        public static InnerClass fromEntity(Accommodation accommodation) {
            String imageUrl = accommodation.getAccommodationImageList()
                .stream()
                .map(AccommodationImageList::fromEntity)
                .map(AccommodationImageList::getUrl)
                .findFirst()
                .orElse(null);
            Integer minimumPrice = accommodation.getRoomList().stream()
                .mapToInt(Room::getPrice)
                .min()
                .orElse(0);

            return InnerClass.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .likeCount(accommodation.getLikeCount())
                .phoneNumber(accommodation.getPhoneNumber())
                .price(minimumPrice)
                .accommodationImageUrl(imageUrl)
                .address(accommodation.getAddress())
                .isAvailable(minimumPrice == 0 ? false : true)
                .build();
        }
    }
}

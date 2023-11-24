package com.aroom.domain.accommodation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import com.aroom.domain.room.repository.RoomImageRepository;
import com.aroom.domain.room.repository.RoomRepository;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AccommodationRepositoryTest {

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomImageRepository roomImageRepository;

    @Autowired
    private AccommodationImageRepository accommodationImageRepository;

    @Test
    @DisplayName("지정된 숙소가 조회 됐습니다.")
    void findAccommodationById() {
        // given
        RoomImage roomImage1 = RoomImage.builder().url(
                "https://www.lottehotel.com/content/dam/lotte-hotel/signiel/seoul/accommodation/premier/180708-30-2000-acc-seoul-signiel.jpg")
            .build();
        RoomImage roomImage2 = RoomImage.builder().url(
                "https://www.lottehotel.com/content/dam/lotte-hotel/signiel/seoul/accommodation/premier/2849-1-2000-roo-LTSG.jpg")
            .build();

        Room room = Room.builder().type("DELUXE").price(350000).capacity(2).maxCapacity(4).checkIn(
                LocalTime.of(15, 0)).checkOut(LocalTime.of(11, 0)).stock(4)
            .roomImageList(Arrays.asList(roomImage1, roomImage2)).build();

        AccommodationImage accommodationImage = AccommodationImage.builder().url(
                "https://www.lottehotel.com/content/dam/lotte-hotel/lotte/seoul/dining/restaurant/pierre-gagnaire/180711-33-2000-din-seoul-hotel.jpg.thumb.768.768.jpg")
            .build();

        Accommodation accommodation = Accommodation.builder()
            .name("롯데호텔").latitude(
                (float) 150.54).longitude((float) 100.5).addressCode("서울특별시 중구 을지로 30")
            .phoneNumber("02-771-1000").roomList(Arrays.asList(room))
            .accommodationImageList(Arrays.asList(accommodationImage)).build();

        roomImageRepository.saveAll(List.of(roomImage1, roomImage2));
        roomRepository.save(room);
        accommodationImageRepository.save(accommodationImage);
        accommodationRepository.save(accommodation);

        // when
        Accommodation findAccommodation = accommodationRepository.findById(accommodation.getId())
            .orElseThrow(AccommodationNotFoundException::new);

        // then
        assertThat(findAccommodation.getName()).isEqualTo("롯데호텔");
        assertThat(findAccommodation.getLatitude()).isEqualTo((float) 150.54);
        assertThat(findAccommodation.getLongitude()).isEqualTo((float) 100.5);
        assertThat(findAccommodation.getAddressCode()).isEqualTo("서울특별시 중구 을지로 30");
        assertThat(findAccommodation.getPhoneNumber()).isEqualTo("02-771-1000");
    }
}

package com.aroom.domain.accommodation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.aroom.domain.accommodation.dto.response.AccommodationResponseDTO;
import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class AccommodationServiceTest {

    @InjectMocks
    private AccommodationService accommodationService;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Nested
    @DisplayName("getSpecificAccommodation()는")
    class Context_getSpecificAccommodation {

        @Test
        @DisplayName("숙소 정보를 상세 조회할 수 있다.")
        void _willSuccess() {
            // given
            RoomImage roomImage1 = RoomImage.builder().url(
                    "https://www.lottehotel.com/content/dam/lotte-hotel/signiel/seoul/accommodation/premier/180708-30-2000-acc-seoul-signiel.jpg")
                .build();
            RoomImage roomImage2 = RoomImage.builder().url(
                    "https://www.lottehotel.com/content/dam/lotte-hotel/signiel/seoul/accommodation/premier/2849-1-2000-roo-LTSG.jpg")
                .build();

            Room room = Room.builder().type("DELUXE").price(350000).capacity(2).maxCapacity(4)
                .checkIn(
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

            given(accommodationRepository.findById(any(Long.TYPE))).willReturn(
                Optional.ofNullable(accommodation));

            // when
            AccommodationResponseDTO result = accommodationService.getRoom(1L);

            // then
            assertThat(result).extracting("accommodationName", "latitude", "longitude",
                    "addressCode", "phoneNumber")
                .containsExactly("롯데호텔", (float) 150.54, (float) 100.5, "서울특별시 중구 을지로 30",
                    "02-771-1000");
            verify(accommodationRepository, times(1)).findById(any(Long.TYPE));
        }


        @Test
        @DisplayName("숙소 정보를 찾을 수 없다면, 상세 조회할 수 없다.")
        void accommodationNotFound_willFail() {
            // given
            Optional<Accommodation> accommodation = Optional.empty();
            given(accommodationRepository.findById(any(Long.TYPE))).willReturn(accommodation);

            // when
            Throwable exception = assertThrows(AccommodationNotFoundException.class, () -> {
                accommodationService.getRoom(1L);
            });

            // then
            assertEquals("숙소를 찾을 수 없습니다.", exception.getMessage());
            verify(accommodationRepository, times(1)).findById(any(Long.TYPE));
        }
    }
}

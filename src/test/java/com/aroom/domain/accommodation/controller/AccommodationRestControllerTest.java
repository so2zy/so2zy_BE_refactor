package com.aroom.domain.accommodation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.dto.response.AccommodationResponseDTO;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.aroom.domain.accommodation.service.AccommodationService;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccommodationRestController.class)
public class AccommodationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccommodationService accommodationService;

    @Nested
    @DisplayName("getSpecificAccommodation()은 ")
    class Context_getSpecificAccommodation {

        @Test
        @DisplayName("숙소 상세 정보를 조회할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            List<RoomImage> roomImageList = Arrays.asList(
                RoomImage.builder().url(
                        "https://www.lottehotel.com/content/dam/lotte-hotel/signiel/seoul/accommodation/premier/180708-30-2000-acc-seoul-signiel.jpg")
                    .build(),
                RoomImage.builder().url(
                        "https://www.lottehotel.com/content/dam/lotte-hotel/signiel/seoul/accommodation/premier/2849-1-2000-roo-LTSG.jpg")
                    .build()
            );
            List<Room> roomList = Arrays.asList(
                Room.builder().type("DELUXE").price(350000).capacity(2).maxCapacity(4).checkIn(
                        LocalTime.of(15, 0)).checkOut(LocalTime.of(11, 0)).stock(4)
                    .roomImageList(roomImageList).build());
            List<AccommodationImage> accommodationImageList = Arrays.asList(
                AccommodationImage.builder().url(
                        "https://www.lottehotel.com/content/dam/lotte-hotel/lotte/seoul/dining/restaurant/pierre-gagnaire/180711-33-2000-din-seoul-hotel.jpg.thumb.768.768.jpg")
                    .build()
            );
            AccommodationResponseDTO accommodationResponseDTO = AccommodationResponseDTO.builder()
                .accommodationName("롯데호텔").latitude(
                    (float) 150.54).longitude((float) 100.5).addressCode("서울특별시 중구 을지로 30")
                .phoneNumber("02-771-1000").roomList(roomList)
                .accommodationImageList(accommodationImageList).build();
            given(accommodationService.getRoom(any(Long.TYPE))).willReturn(
                accommodationResponseDTO);

            // when, then
            mockMvc.perform(get("/v1/accommodations/{accommodation_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accommodationName").isString())
                .andExpect(jsonPath("$.data.roomList").isArray())
                .andExpect(jsonPath("$.data.accommodationImageList").isArray()).andDo(print());
            verify(accommodationService, times(1)).getRoom(any(Long.TYPE));
        }
    }
}

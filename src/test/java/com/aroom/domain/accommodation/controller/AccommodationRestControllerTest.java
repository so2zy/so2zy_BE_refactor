package com.aroom.domain.accommodation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.dto.response.RoomListInfoResponse;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.aroom.util.ControllerTestWithoutSecurityHelper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AccommodationRestControllerTest extends ControllerTestWithoutSecurityHelper {

    @Test
    void testFindAllAccommodationWithSearchCondition() throws Exception {
        // given
        SearchCondition mockSearchCondition = SearchCondition.builder()
            .orderCondition(null)
            .orderBy(null)
            .checkOut(null)
            .name(null)
            .checkIn(null)
            .lowestPrice(null)
            .highestPrice(null)
            .addressCode(null)
            .likeCount(null)
            .phoneNumber(null)
            .build();
        PageRequest mockPageRequest = PageRequest.of(0, 10);
        Sort mockSortCondition = Sort.by(Direction.ASC,
            AccommodationRestController.NO_ORDER_CONDITION);

        List<AccommodationListResponse> accommodationListResponse = List.of(
            AccommodationListResponse.builder()
                .id(1L)
                .latitude(1)
                .accommodationImageLists(null)
                .phoneNumber("02-000-000")
                .longitude(1)
                .name("야놀자호텔")
                .addressCode("서울시 강남구 청담동 1111")
                .build());
        when(accommodationService.getAccommodationListBySearchCondition(mockSearchCondition,
            mockPageRequest, mockSortCondition))
            .thenReturn(accommodationListResponse);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accommodations")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockSearchCondition)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("숙소 정보를 성공적으로 조회했습니다."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());

    }

    @Nested
    @DisplayName("getSpecificAccommodation()은 ")
    class Context_getSpecificAccommodation {

        @Test
        @DisplayName("숙소 상세 정보를 조회할 수 있다.")
        void _willSuccess() throws Exception {
            // given
            List<RoomListInfoResponse> roomList = Arrays.asList(
                RoomListInfoResponse.builder().type("DELUXE").price(350000).capacity(2).maxCapacity(4)
                    .checkIn("15:00").checkOut("11:00")
                    .url("naver.com").build());

            AccommodationResponse accommodationResponse = AccommodationResponse.builder()
                .accommodationName("롯데호텔").latitude(
                    (float) 150.54).longitude((float) 100.5).addressCode("서울특별시 중구 을지로 30")
                .phoneNumber("02-771-1000").roomInfoList(roomList)
                .accommodationUrl("https://www.lottehotel.com/content/dam/lotte-hotel/lotte/seoul/dining/restaurant/pierre-gagnaire/180711-33-2000-din-seoul-hotel.jpg.thumb.768.768.jpg").build();
            given(accommodationService.getRoom(any(Long.TYPE))).willReturn(
                accommodationResponse);

            // when, then
            mockMvc.perform(get("/v1/accommodations/{accommodation_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accommodationName").isString())
                .andExpect(jsonPath("$.data.roomInfoList").isArray()).andDo(print());
            verify(accommodationService, times(1)).getRoom(any(Long.TYPE));
        }
    }
}


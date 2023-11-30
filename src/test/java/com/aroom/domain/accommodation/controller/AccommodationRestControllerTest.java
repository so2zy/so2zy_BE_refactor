package com.aroom.domain.accommodation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.dto.AccommodationImageList;
import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.AccommodationListResponse.InnerClass;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.dto.response.RoomListInfoResponse;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.address.model.Address;
import com.aroom.domain.room.model.Room;
import com.aroom.util.ControllerTestWithoutSecurityHelper;
import com.aroom.util.security.WithMockAccountContext;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;

class AccommodationRestControllerTest extends ControllerTestWithoutSecurityHelper {

    protected MediaType contentType =
        new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);
    private AccommodationListResponse accommodationListResponse;
    private AccommodationListResponse.InnerClass innerClass;
    private AccommodationListResponse.InnerClass innerClass2;
    private Accommodation accommodation;
    private Room room;
    private AccommodationImageList accommodationImageList;
    private Address addressEntity;

    @BeforeEach
    private void init() {
        addressEntity = Address.builder()
            .id(1L)
            .areaCode(1)
            .areaName("서울시")
            .sigunguCode(18)
            .sigunguName("송파구")
            .build();
        accommodationImageList = AccommodationImageList.builder()
            .id(1L)
            .url("image.com")
            .build();
        room = Room.builder()
            .id(1L)
            .type("DELUXE")
            .checkIn(LocalTime.of(20, 00))
            .checkOut(LocalTime.of(13, 00))
            .price(110000)
            .capacity(4)
            .maxCapacity(6)
            .soldCount(10)
            .build();
        accommodation = Accommodation.builder()
            .id(1L)
            .name("롯데호텔")
            .likeCount(11)
            .phoneNumber("02-111-111")
            .longitude(30)
            .latitude(30)
            .roomList(List.of(room))
            .addressEntity(addressEntity)
            .accommodationImageList(null)
            .build();
        innerClass = InnerClass.builder()
            .id(1L)
            .name("롯데호텔")
            .likeCount(11)
            .phoneNumber("02-111-111")
            .longitude(30)
            .latitude(30)
            .accommodationImageUrl("www.com")
            .price(110000)
            .build();
        accommodationListResponse = AccommodationListResponse.builder()
            .page(0)
            .size(10)
            .body(List.of(innerClass))
            .build();
    }

    @Nested
    @DisplayName("숙소 검색은")
    class getAccommodation {

        @Test
        @DisplayName("검색조건이 없는 경우 전체를 반환한다")
        void get_accommodation_with_no_search_condition() throws Exception {
            PageRequest pageable = PageRequest.of(0, 10);
            //given
            given(accommodationService.getAllAccommodation(any()))
                .willReturn(accommodationListResponse);

            //when
            AccommodationListResponse testResponse = accommodationService.getAllAccommodation(
                pageable);


            System.out.println(testResponse.getBody().get(0).getName());

            //then
            mockMvc.perform(get("/v2/accommodations")
                    .contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.page").isNumber())
                .andExpect(jsonPath("$.data.size").isNumber())
                .andExpect(jsonPath("$.data.body").isArray())
                .andExpect(jsonPath("$.data.body[0].name").isString());

            //Mockito.verify(accommodationService, times(1)).getAllAccommodation(pageable);
        }

        @Test
        @DisplayName("검색조건이 있는 경우 검색 조건에 맞는 숙소를 반환한다")
        void get_accommodation_with_search_condition() throws Exception {
            innerClass2 = InnerClass.builder()
                .id(1L)
                .name("영주")
                .likeCount(123)
                .phoneNumber("054-111-111")
                .longitude(30)
                .latitude(30)
                .accommodationImageUrl("www.com")
                .price(200000)
                .build();

            AccommodationListResponse accommodationListResponse1 = AccommodationListResponse.builder()
                .page(0)
                .size(10)
                .body(List.of(innerClass))
                .build();
            PageRequest pageable = PageRequest.of(0, 10);
            //given
            given(accommodationService.getAccommodationListBySearchCondition(any(), any(), any()))
                .willReturn(accommodationListResponse1);
            SearchCondition searchCondition = SearchCondition.builder()
                .name("롯데")
                .build();

            //when
            AccommodationListResponse testResponse = accommodationService.getAccommodationListBySearchCondition(
                searchCondition,
                pageable,
                null);


            System.out.println(testResponse.getBody().get(0).getName());

            //then
            mockMvc.perform(get("/v2/accommodations")
                    .queryParam("name","롯데")
                    .contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.page").isNumber())
                .andExpect(jsonPath("$.data.size").isNumber())
                .andExpect(jsonPath("$.data.body").isArray())
                .andExpect(jsonPath("$.data.body[0].name").isString());

            //Mockito.verify(accommodationService, times(1)).getAllAccommodation(pageable);
        }
    }

    @Nested
    @DisplayName("getSpecificAccommodation()은 ")
    class Context_getSpecificAccommodation {

        @Test
        @DisplayName("숙소 상세 정보를 조회할 수 있다.")
        @WithMockAccountContext
        void _willSuccess() throws Exception {
            // given
            List<RoomListInfoResponse> roomList = Arrays.asList(
                RoomListInfoResponse.builder().type("DELUXE").price(350000).capacity(2)
                    .maxCapacity(4)
                    .checkIn("15:00").checkOut("11:00")
                    .url("naver.com").stock(4).build());

            AccommodationResponse accommodationResponse = AccommodationResponse.builder()
                .accommodationName("롯데호텔").latitude(
                    (float) 150.54).longitude((float) 100.5).address("경기도 고양시 일산동구")
                .phoneNumber("02-771-1000").roomInfoList(roomList)
                .accommodationUrl(
                    "https://www.lottehotel.com/content/dam/lotte-hotel/lotte/seoul/dining/restaurant/pierre-gagnaire/180711-33-2000-din-seoul-hotel.jpg.thumb.768.768.jpg")
                .build();
            given(accommodationService.getRoom(any(Long.TYPE), any(), any(), any(),
                any())).willReturn(
                accommodationResponse);

            // when, then
            mockMvc.perform(
                    get("/v2/accommodations/{accommodation_id}", 1L).param("startDate", "2023-11-28")
                        .param("endDate", "2023-11-29").param("personnel", String.valueOf(3))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accommodationName").isString())
                .andExpect(jsonPath("$.data.roomInfoList").isArray()).andDo(print());
        }
    }
}


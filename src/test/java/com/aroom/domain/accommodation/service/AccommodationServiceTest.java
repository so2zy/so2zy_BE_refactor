package com.aroom.domain.accommodation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.AccommodationListResponse.InnerClass;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationOnlyResponse;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.model.AccommodationImage;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import com.aroom.domain.address.model.Address;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class AccommodationServiceTest {

    @InjectMocks
    private AccommodationService accommodationService;

    @Mock
    private AccommodationRepository accommodationRepository;

    private Address address;
    private AccommodationImage accommodationImage;
    private RoomImage roomImage;
    private Room room;
    private Accommodation accommodation;

    private Accommodation accommodation2;

    private InnerClass innerClass;

    private AccommodationListResponse accommodationListResponse;



    @BeforeEach
    private void init() {
        address = Address.builder().id(1L).areaCode(1).sigunguCode(1).areaName("경기도")
            .sigunguName("고양시").build();

        accommodationImage = AccommodationImage.builder().url("naver.com").build();

        roomImage = RoomImage.builder().url("google.com").build();

        room = Room.builder().id(1L).type("DELUXE").price(350000).capacity(2)
            .maxCapacity(4)
            .checkIn(
                LocalTime.of(15, 0)).checkOut(LocalTime.of(11, 0)).roomImageList(List.of(roomImage))
            .build();

        accommodation = Accommodation.builder().id(1L)
            .addressEntity(address)
            .name("롯데호텔").latitude(
                (float) 150.54).longitude((float) 100.5)
            .phoneNumber("02-771-1000").address("경기도 고양시 일산동구").roomList(List.of(room))
            .accommodationImageList(List.of(accommodationImage)).build();


        accommodation2 = Accommodation.builder()
            .id(2L)
            .name("영주호텔")
            .likeCount(22)
            .phoneNumber("054-111-111")
            .longitude(30)
            .latitude(30)
            .roomList(List.of(room))
            .addressEntity(address)
            .accommodationImageList(List.of(accommodationImage))
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
    @DisplayName("숙소 조회를 할 때")
    class getAccommodation {
        @Test
        @DisplayName("검색조건이 없는 경우")
        void get_accommodation_with_no_search_condition() throws Exception {
            PageRequest pageable = PageRequest.of(0, 10);
            List<Accommodation> mockAccommodations = Arrays.asList(accommodation, accommodation2);
            given(accommodationRepository.getAll(any()))
                .willReturn(mockAccommodations);
            //when
            AccommodationListResponse result = accommodationService.getAllAccommodation(pageable);

            System.out.println(result.getBody().get(0).getName());
            //then
            assertEquals(result.getBody().get(0).getLikeCount(), 0);
            assertEquals(result.getBody().get(0).getName(), "롯데호텔");
        }
        @Test
        @DisplayName("검색조건이 있는 경우")
        void get_accommodation_with_search_condition() throws Exception {
            // Given
            SearchCondition searchCondition = SearchCondition.builder()
                .name("영주")
                .build();
            Pageable pageable = PageRequest.of(0, 10);
            Sort sortCondition = Sort.by(Sort.Direction.ASC, "default");
            List<Accommodation> mockAccommodations = Arrays.asList(accommodation2);
            // Mock the repository behavior
            when(accommodationRepository.getAccommodationBySearchCondition(any(), any()))
                .thenReturn(mockAccommodations);
            AccommodationListResponse accommodationListResponse = accommodationService.getAccommodationListBySearchCondition(
                searchCondition, pageable, sortCondition);
            assertEquals(accommodationListResponse.getBody().size(), 1);
            assertEquals(accommodationListResponse.getBody().get(0).getName(), "영주호텔");
        }

        @Test
        @DisplayName("정렬조건이 있는 경우")
        void get_accommodation_with_sort_condition() throws Exception {
            // Given
            SearchCondition searchCondition = SearchCondition.builder()
                .name("영주")
                .orderCondition("likeCount")
                .orderBy("asc")
                .build();
            Pageable pageable = PageRequest.of(0, 10);
            Sort sortCondition = Sort.by(Sort.Direction.ASC, "likeCount");
            List<Accommodation> mockAccommodations = Arrays.asList(accommodation2);
            // Mock the repository behavior
            when(accommodationRepository.getAccommodationBySearchConditionWithSortCondition(any(),
                any(), any()))
                .thenReturn(mockAccommodations);
            AccommodationListResponse accommodationListResponse = accommodationService.getAccommodationListBySearchCondition(
                searchCondition, pageable, sortCondition);
            assertEquals(accommodationListResponse.getBody().size(), 1);
            assertEquals(accommodationListResponse.getBody().get(0).getName(), "영주호텔");
        }
    }

    @Nested
    @DisplayName("getSpecificAccommodation()는")
    class Context_getSpecificAccommodation {

        @Test
        @DisplayName("숙소 정보를 상세 조회할 수 있다.")
        void _willSuccess() {
            // given
            long accommodationId = 1L;
            String startDate = "2023-11-27";
            String endDate = "2023-11-28";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateTypeChanged = LocalDate.parse(startDate, formatter);
            LocalDate endDateTypeChanged = LocalDate.parse(endDate, formatter);
            int personnel = 3;
            long memberId = 1L;

            given(
                accommodationRepository.findByAccommodationIdAndStartDate(any(Long.TYPE),
                    eq(startDateTypeChanged),
                    eq(endDateTypeChanged))).willReturn(
                Optional.ofNullable(accommodation));

            given(accommodationRepository.findByAccommodationIdAndMemberId(any(Long.TYPE),
                any(Long.TYPE))).willReturn(Optional.ofNullable(accommodation));

            // when
            Object result = accommodationService.getRoom(1L, startDate, endDate,
                personnel, memberId);

            // then
            assertThat(result).isInstanceOf(AccommodationResponse.class);
            assertThat(result).extracting("accommodationName", "latitude", "longitude",
                    "phoneNumber")
                .containsExactly("롯데호텔", (float) 150.54, (float) 100.5, "02-771-1000");
        }


        @Test
        @DisplayName("숙소 정보를 찾을 수 없다면, 상세 조회할 수 없다.")
        void accommodationNotFound_willFail() {
            // given
            long accommodationId = 1L;
            String startDate = "2023-11-27";
            String endDate = "2023-11-28";
            int personnel = 3;
            long memberId = 1L;

            given(accommodationRepository.findById(any(Long.TYPE))).willReturn(
                Optional.ofNullable(accommodation));

            given(accommodationRepository.findByAccommodationIdAndMemberId(any(Long.TYPE),
                any(Long.TYPE))).willReturn(Optional.ofNullable(accommodation));

            // when
            Object result = accommodationService.getRoom(accommodationId, startDate, endDate,
                personnel, memberId);

            // then
            assertThat(result).isInstanceOf(AccommodationOnlyResponse.class);
            assertThat(result).extracting("accommodationName", "latitude", "longitude",
                    "phoneNumber")
                .containsExactly("롯데호텔", (float) 150.54, (float) 100.5, "02-771-1000");
        }
    }
}

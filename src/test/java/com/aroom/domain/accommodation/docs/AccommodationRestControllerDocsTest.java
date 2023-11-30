package com.aroom.domain.accommodation.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.controller.AccommodationRestController;
import com.aroom.domain.accommodation.dto.AccommodationImageList;
import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.AccommodationListResponse.InnerClass;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.dto.response.RoomListInfoResponse;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.service.AccommodationService;
import com.aroom.domain.address.model.Address;
import com.aroom.domain.room.model.Room;
import com.aroom.global.config.CustomHttpHeaders;
import com.aroom.util.docs.RestDocsHelper;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class AccommodationRestControllerDocsTest extends RestDocsHelper {

    private final AccommodationService accommodationService = mock(AccommodationService.class);

    @Override
    public Object initController() {
        return new AccommodationRestController((accommodationService));
    }
    @Test
    @DisplayName("숙소 전체 조회 API 문서화")
    void getAllAccommodation() throws Exception{

        AccommodationListResponse.InnerClass innerClass = InnerClass.builder()
            .id(1L)
            .name("롯데호텔")
            .likeCount(11)
            .phoneNumber("02-111-111")
            .longitude(30)
            .latitude(30)
            .address("서울시 강남구 111")
            .accommodationImageUrl("www.com")
            .price(110000)
            .isAvailable(true)
            .build();
        AccommodationListResponse accommodationListResponse = AccommodationListResponse.builder()
            .page(0)
            .size(10)
            .body(List.of(innerClass))
            .build();

        //given
        PageRequest pageable = PageRequest.of(0, 10);

        given(accommodationService.getAllAccommodation(any()))
            .willReturn(accommodationListResponse);

        AccommodationListResponse testResponse = accommodationService.getAllAccommodation(
            pageable);

        mockMvc.perform(MockMvcRequestBuilders.get("/v2/accommodations")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("accommodation-all-get"
            ,responseFields(
                fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                    .description("응답 상세 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                    .description("응답 데이터"),
                    fieldWithPath("data.page").type(JsonFieldType.NUMBER).description("페이지 넘버"),
                    fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                    fieldWithPath("data.body").type(JsonFieldType.ARRAY).description("응답 바디"),
                    fieldWithPath("data.body[0].id").type(JsonFieldType.NUMBER).description("숙소번호"),
                    fieldWithPath("data.body[0].name").type(JsonFieldType.STRING).description("숙소 이름"),
                    fieldWithPath("data.body[0].latitude").type(JsonFieldType.NUMBER).description("위도"),
                    fieldWithPath("data.body[0].longitude").type(JsonFieldType.NUMBER).description("경도"),
                    fieldWithPath("data.body[0].address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("data.body[0].likeCount").type(JsonFieldType.NUMBER).description("찜 개수"),
                    fieldWithPath("data.body[0].phoneNumber").type(JsonFieldType.STRING).description("숙소 전화번호"),
                    fieldWithPath("data.body[0].accommodationImageUrl").type(JsonFieldType.STRING).description("숙소 이미지 URL"),
                    fieldWithPath("data.body[0].isAvailable").type(JsonFieldType.BOOLEAN).description("예약 가능 숙소 여부"),
                    fieldWithPath("data.body[0].price").type(JsonFieldType.NUMBER).description("숙소 대표 가격(최저가)"))));

    }

    @Test
    @DisplayName("숙소 필터링 조회 API 문서화")
    void get_specific_accommodation_by_filtering() throws Exception{

        AccommodationListResponse.InnerClass innerClass = InnerClass.builder()
            .id(1L)
            .name("롯데호텔")
            .likeCount(11)
            .phoneNumber("02-111-111")
            .longitude(30)
            .latitude(30)
            .address("서울시 강남구 111")
            .accommodationImageUrl("www.com")
            .price(110000)
            .isAvailable(true)
            .build();
        AccommodationListResponse.InnerClass innerClass2 = InnerClass.builder()
            .id(1L)
            .name("영주호텔")
            .likeCount(22)
            .phoneNumber("054-111-111")
            .longitude(30)
            .latitude(30)
            .address("경북 영주시 111")
            .accommodationImageUrl("www.com")
            .price(220000)
            .isAvailable(true)
            .build();
        AccommodationListResponse accommodationListResponse = AccommodationListResponse.builder()
            .page(0)
            .size(10)
            .body(List.of(innerClass, innerClass2))
            .build();

        //given
        PageRequest pageable = PageRequest.of(0, 10);
        //given
        given(accommodationService.getAccommodationListBySearchCondition(any(), any(), any()))
            .willReturn(accommodationListResponse);
        SearchCondition searchCondition = SearchCondition.builder()
            .name("롯데")
            .build();

        //when
        AccommodationListResponse testResponse = accommodationService.getAccommodationListBySearchCondition(
            searchCondition,
            pageable,
            null);

        mockMvc.perform(MockMvcRequestBuilders.get("/v2/accommodations")
                .queryParam("name", "롯데")
                .queryParam("areaName", "서울시")
                .queryParam("sigunguName", "강남구")
                .queryParam("likeCount", "10")
                .queryParam("lowestPrice", "0")
                .queryParam("highestPrice", "100000")
                .queryParam("checkIn", "20:00")
                .queryParam("checkOut", "13:00")
                .queryParam("startDate", "2023-12-01")
                .queryParam("endDate", "2023-12-10")
                .queryParam("orderBy", "asc")
                .queryParam("orderCondition", "price")



                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("accommodation-filtering-get"
                ,queryParameters(
                    parameterWithName("name").description("숙소이름"),
                    parameterWithName("areaName").description("지역 명"),
                    parameterWithName("sigunguName").description("시군구 명"),
                    parameterWithName("likeCount").description("찜 개수"),
                    parameterWithName("lowestPrice").description("특정 가격 이상"),
                    parameterWithName("highestPrice").description("특정 가격 이하"),
                    parameterWithName("checkIn").description("체크인 시간이 특정 시간 이후"),
                    parameterWithName("checkOut").description("체크아웃 시간이 특정 시간 이전"),
                    parameterWithName("startDate").description("예약 가능 날짜가 특정 날짜 이후"),
                    parameterWithName("endDate").description("예약 가능 날짜가 특정 날짜 이전"),
                    parameterWithName("orderBy").description("오름차순(asc), 내림차순(desc)"),
                    parameterWithName("orderCondition").description("정렬 조건 (price, likeCount, capacity, soldCount)")
                )
                ,responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"),
                    fieldWithPath("data.page").type(JsonFieldType.NUMBER).description("페이지 넘버"),
                    fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                    fieldWithPath("data.body").type(JsonFieldType.ARRAY).description("응답 바디"),
                    fieldWithPath("data.body[0].id").type(JsonFieldType.NUMBER).description("숙소번호"),
                    fieldWithPath("data.body[0].name").type(JsonFieldType.STRING).description("숙소 이름"),
                    fieldWithPath("data.body[0].latitude").type(JsonFieldType.NUMBER).description("위도"),
                    fieldWithPath("data.body[0].longitude").type(JsonFieldType.NUMBER).description("경도"),
                    fieldWithPath("data.body[0].address").type(JsonFieldType.STRING).description("주소"),
                    fieldWithPath("data.body[0].likeCount").type(JsonFieldType.NUMBER).description("찜 개수"),
                    fieldWithPath("data.body[0].phoneNumber").type(JsonFieldType.STRING).description("숙소 전화번호"),
                    fieldWithPath("data.body[0].accommodationImageUrl").type(JsonFieldType.STRING).description("숙소 이미지 URL"),
                    fieldWithPath("data.body[0].isAvailable").type(JsonFieldType.BOOLEAN).description("예약 가능 숙소 여부"),
                    fieldWithPath("data.body[0].price").type(JsonFieldType.NUMBER).description("숙소 대표 가격(최저가)"))));

    }

    @Test
    @DisplayName("숙소 상세 조회 API 문서화")
    void getSpecificAccommodation() throws Exception {
        // given
        List<RoomListInfoResponse> roomList = Arrays.asList(
            RoomListInfoResponse.builder().id(1L).type("DELUXE").price(350000).capacity(2)
                .maxCapacity(4)
                .checkIn("15:00").checkOut("11:00")
                .url("https://www.lottehotel.com/content/dam/lotte-hotel/signiel/seoul/accommodation/deluxe/2838-01-2000-roo-LTSG.jpg").stock(4).build());

        AccommodationResponse accommodationResponse = AccommodationResponse.builder().id(1L)
            .accommodationName("롯데호텔").latitude(
                (float) 150.54).longitude((float) 100.5).address("서울특별시 중구 을지로 30")
            .phoneNumber("02-771-1000").accommodationUrl(
                "https://www.lottehotel.com/content/dam/lotte-hotel/lotte/seoul/dining/restaurant/pierre-gagnaire/180711-33-2000-din-seoul-hotel.jpg.thumb.768.768.jpg")
            .favorite(true).roomInfoList(roomList).build();
        given(accommodationService.getRoom(any(Long.TYPE), any(), any(), any(),
            any())).willReturn(
            accommodationResponse);

        // when, then
        mockMvc.perform(get("/v2/accommodations/{accommodation_id}", 1L).param("startDate", "2023-11-28")
                    .param("endDate", "2023-11-29").param("personnel", String.valueOf(3))
                .header(CustomHttpHeaders.ACCESS_TOKEN,"eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLWtleSI6IjEiLCJ1c2VyLW5hbWUiOiLslpHsnKDrprwiLCJpc3MiOiJBcm9vbS1CRSIsImlhdCI6MTcwMTE3NjgyOCwiZXhwIjoxNzAxMjEyODI4fQ.3tJ8qpUQ3ajYYt7_mop7LHV37hUCXP9kElmItpkvwK2nLvE_kJe9-Xm4FHLdKmmHT5EW_uCIA-otQDHZRfImBA")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("accommodation-specific-get",
                pathParameters(parameterWithName("accommodation_id").description("숙소 식별자")),
                queryParameters(
                    parameterWithName("startDate").description("시작날짜"),
                    parameterWithName("endDate").description("종료날짜"),
                    parameterWithName("personnel").description("인원")
                ),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("숙소 식별자"),
                    fieldWithPath("data.accommodationName").type(JsonFieldType.STRING).description("숙소 이름"),
                    fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("숙소 위도"),
                    fieldWithPath("data.longitude").type(JsonFieldType.NUMBER).description("숙소 경도"),
                    fieldWithPath("data.address").type(JsonFieldType.STRING).description("숙소 주소"),
                    fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("숙소 전화번호"),
                    fieldWithPath("data.accommodationUrl").type(JsonFieldType.STRING).description("숙소 이미지 url"),
                    fieldWithPath("data.favorite").type(JsonFieldType.BOOLEAN).description("숙소 찜 유무"),
                    fieldWithPath("data.roomInfoList").type(JsonFieldType.ARRAY).description("객실 정보"),
                    fieldWithPath("data.roomInfoList[].id").type(JsonFieldType.NUMBER).description("객실 식별자"),
                    fieldWithPath("data.roomInfoList[].type").type(JsonFieldType.STRING).description("객실 이름"),
                    fieldWithPath("data.roomInfoList[].price").type(JsonFieldType.NUMBER).description("객실 가격"),
                    fieldWithPath("data.roomInfoList[].capacity").type(JsonFieldType.NUMBER).description("객실 인원"),
                    fieldWithPath("data.roomInfoList[].maxCapacity").type(JsonFieldType.NUMBER).description("객실 최대 수용 인원"),
                    fieldWithPath("data.roomInfoList[].checkIn").type(JsonFieldType.STRING).description("객실 체크인"),
                    fieldWithPath("data.roomInfoList[].checkOut").type(JsonFieldType.STRING).description("객실 체크아웃"),
                    fieldWithPath("data.roomInfoList[].url").type(JsonFieldType.STRING).description("객실 이미지 url"),
                    fieldWithPath("data.roomInfoList[].stock").type(JsonFieldType.NUMBER).description("객실 재고"))
                ));
    }
}


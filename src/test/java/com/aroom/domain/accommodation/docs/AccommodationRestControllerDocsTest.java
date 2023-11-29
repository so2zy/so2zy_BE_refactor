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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.controller.AccommodationRestController;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.dto.response.RoomListInfoResponse;
import com.aroom.domain.accommodation.service.AccommodationService;
import com.aroom.global.config.CustomHttpHeaders;
import com.aroom.util.docs.RestDocsHelper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class AccommodationRestControllerDocsTest extends RestDocsHelper {

    private final AccommodationService accommodationService = mock(AccommodationService.class);

    @Override
    public Object initController() {
        return new AccommodationRestController((accommodationService));
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


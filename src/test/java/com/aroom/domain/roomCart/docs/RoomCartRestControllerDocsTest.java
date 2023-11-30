package com.aroom.domain.roomCart.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.dto.response.CartAccommodationResponse;
import com.aroom.domain.room.dto.response.CartRoomResponse;
import com.aroom.domain.roomCart.controller.RoomCartRestController;
import com.aroom.domain.roomCart.dto.request.RoomCartRequest;
import com.aroom.domain.roomCart.dto.response.FindCartResponse;
import com.aroom.domain.roomCart.dto.response.RoomCartInfoResponse;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.service.RoomCartService;
import com.aroom.global.config.CustomHttpHeaders;
import com.aroom.util.docs.RestDocsHelper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class RoomCartRestControllerDocsTest extends RestDocsHelper {

    private final RoomCartService roomCartService = mock(RoomCartService.class);

    @Override
    public Object initController() {
        return new RoomCartRestController(roomCartService);
    }

    private final ConstraintDescriptions postDescriptions = new ConstraintDescriptions(
        RoomCartRequest.class);

    @Test
    @DisplayName("객실 장바구니 추가 API 문서화")
    void postRoomCart() throws Exception {
        // given
        RoomCartRequest roomCartRequest = RoomCartRequest.builder()
            .startDate(LocalDate.of(2023, 11, 27)).endDate(LocalDate.of(2023, 11, 28)).personnel(3).build();

        List<RoomCartInfoResponse> roomCartInfoResponseList = Arrays.asList(
            RoomCartInfoResponse.builder().room_id(1L).cart_id(1L).build(),
            RoomCartInfoResponse.builder().room_id(1L).cart_id(1L).build()
        );

        RoomCartResponse roomCartResponse = RoomCartResponse.builder()
            .roomCartList(roomCartInfoResponseList).build();

        given(roomCartService.postRoomCart(any(Long.TYPE), any(Long.TYPE),
            any(RoomCartRequest.class))).willReturn(
            roomCartResponse);

        // when, then
        mockMvc.perform(post("/v2/carts/{room_id}", 1L).header(CustomHttpHeaders.ACCESS_TOKEN,
                    "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLWtleSI6IjEiLCJ1c2VyLW5hbWUiOiLslpHsnKDrprwiLCJpc3MiOiJBcm9vbS1CRSIsImlhdCI6MTcwMTE3NjgyOCwiZXhwIjoxNzAxMjEyODI4fQ.3tJ8qpUQ3ajYYt7_mop7LHV37hUCXP9kElmItpkvwK2nLvE_kJe9-Xm4FHLdKmmHT5EW_uCIA-otQDHZRfImBA")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomCartRequest)))
            .andExpect(status().isCreated())
            .andDo(document("roomCart-post",
                pathParameters(parameterWithName("room_id").description("객실 식별자")),
                requestFields(
                    fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 일자")
                        .attributes(key("constraints").value(
                            postDescriptions.descriptionsForProperty("startDate"))),
                    fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 일자")
                        .attributes(key("constraints").value(
                            postDescriptions.descriptionsForProperty("endDate"))),
                    fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 일자")
                        .attributes(key("constraints").value(
                            postDescriptions.descriptionsForProperty("endDate"))),
                    fieldWithPath("personnel").type(JsonFieldType.NUMBER).description("인원")
                        .attributes(key("constraints").value(
                            postDescriptions.descriptionsForProperty("personnel")))
                    ),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"),
                    fieldWithPath("data.roomCartList").type(JsonFieldType.ARRAY)
                        .description("객실 장바구니 정보"),
                    fieldWithPath("data.roomCartList[].room_id").type(JsonFieldType.NUMBER)
                        .description("객실 식별자"),
                    fieldWithPath("data.roomCartList[].cart_id").type(JsonFieldType.NUMBER)
                        .description("장바구니 식별자"))
            ));
    }

    @Test
    @DisplayName("장바구니 조회 API 문서화")
    void getRoomCart() throws Exception {
        // given
        CartRoomResponse cartRoomResponse = CartRoomResponse.builder()
            .roomId(1L)
            .type("p1")
            .checkIn(LocalTime.of(10, 00, 00))
            .checkOut(LocalTime.of(11, 00, 00))
            .capacity(2)
            .maxCapacity(4)
            .price(77000)
            .startDate(LocalDate.of(2023, 12, 10))
            .endDate(LocalDate.of(2023, 12, 11))
            .roomImageUrl("asdagnasod.com")
            .build();

        CartAccommodationResponse cartAccommodationResponse = CartAccommodationResponse.builder()
            .accommodationId(1L)
            .accommodationName("남해 글리드 풀빌라")
            .address("경상북도 남해군 남면")
            .roomList(List.of(cartRoomResponse))
            .build();

        FindCartResponse findCartResponse = FindCartResponse.builder()
            .accommodationList(List.of(cartAccommodationResponse))
            .build();

        given(roomCartService.getCartList(any())).willReturn(findCartResponse);


        // when
        mockMvc.perform(get("/v2/carts")
            .header(CustomHttpHeaders.ACCESS_TOKEN,
                "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLWtleSI6IjEiLCJ1c2VyLW5hbWUiOiLslpHsnKDrprwiLCJpc3MiOiJBcm9vbS1CRSIsImlhdCI6MTcwMTE3NjgyOCwiZXhwIjoxNzAxMjEyODI4fQ.3tJ8qpUQ3ajYYt7_mop7LHV37hUCXP9kElmItpkvwK2nLvE_kJe9-Xm4FHLdKmmHT5EW_uCIA-otQDHZRfImBA")
        ).andExpect(status().isOk())
            .andDo(
                document("roomCart-get",
            responseFields(
                fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                    .description("응답 상세 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                    .description("응답 데이터"),
                fieldWithPath("data.accommodationList").type(JsonFieldType.ARRAY)
                    .description("숙소 정보"),
                fieldWithPath("data.accommodationList[].accommodationId").type(JsonFieldType.NUMBER)
                    .description("숙소 식별자"),
                fieldWithPath("data.accommodationList[].accommodationName").type(JsonFieldType.STRING)
                    .description("숙소 이름"),
                fieldWithPath("data.accommodationList[].address").type(JsonFieldType.STRING)
                    .description("숙소 상세 주소"),
                fieldWithPath("data.accommodationList[].roomList").type(JsonFieldType.ARRAY)
                    .description("객실 정보"),
                fieldWithPath("data.accommodationList[].roomList[].roomId").type(JsonFieldType.NUMBER)
                    .description("객실 식별자"),
                fieldWithPath("data.accommodationList[].roomList[].type").type(JsonFieldType.STRING)
                    .description("객실 타입"),
                fieldWithPath("data.accommodationList[].roomList[].checkIn").type(JsonFieldType.STRING)
                    .description("체크인 시간"),
                fieldWithPath("data.accommodationList[].roomList[].checkOut").type(JsonFieldType.STRING)
                    .description("체크아웃 시간"),
                fieldWithPath("data.accommodationList[].roomList[].capacity").type(JsonFieldType.NUMBER)
                    .description("기본 인원"),
                fieldWithPath("data.accommodationList[].roomList[].maxCapacity").type(JsonFieldType.NUMBER)
                    .description("최대 인원"),
                fieldWithPath("data.accommodationList[].roomList[].price").type(JsonFieldType.NUMBER)
                    .description("1박당 가격"),
                fieldWithPath("data.accommodationList[].roomList[].startDate").type(JsonFieldType.STRING)
                    .description("예약 시작 일"),
                fieldWithPath("data.accommodationList[].roomList[].endDate").type(JsonFieldType.STRING)
                    .description("예약 종료 일"),
                fieldWithPath("data.accommodationList[].roomList[].roomImageUrl").type(JsonFieldType.STRING)
                    .description("객실 이미지 url"),
                fieldWithPath("data.accommodationList[].roomList[].personnel").type(JsonFieldType.NUMBER)
                    .description("객실 인원"))
            ));
    }
}

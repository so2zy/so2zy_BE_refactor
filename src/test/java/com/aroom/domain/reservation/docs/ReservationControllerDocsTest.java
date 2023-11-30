package com.aroom.domain.reservation.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.reservation.controller.ReservationRestController;
import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.service.ReservationService;
import com.aroom.global.config.CustomHttpHeaders;
import com.aroom.util.docs.RestDocsHelper;
import com.aroom.util.fixture.JwtFixture;
import com.aroom.util.fixture.ReservationFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class ReservationControllerDocsTest extends RestDocsHelper {

    private final ReservationService reservationService = mock(ReservationService.class);

    @Override
    public Object initController() {
        return new ReservationRestController(reservationService);
    }

    @Test
    @DisplayName("예약 문서화")
    void reservationRoom_documentation_willSuccess() throws Exception {
        // given
        ReservationRequest request = ReservationFixture.getReservationRequest(
            List.of(ReservationFixture.getRoomReservationRequest()));

        given(reservationService.reserveRoom(any(), any())).willReturn(
            ReservationFixture.getReservationResponse(
                List.of(ReservationFixture.getRoomReservationResponse())));

        // when then
        mockMvc.perform(post("/v1/reservations")
                .header(CustomHttpHeaders.ACCESS_TOKEN, JwtFixture.getTokenFixture())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(document("reservations-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("roomList[]").type(JsonFieldType.ARRAY).description("예약할 방 목록")
                        .attributes(key("constraints").value("최소 방은 하나 이상")),
                    fieldWithPath("roomList[].roomId").type(JsonFieldType.NUMBER)
                        .description("방 식별자(ID)"),
                    fieldWithPath("roomList[].startDate").type(JsonFieldType.STRING)
                        .description("예약 시작일")
                        .attributes(key("constraints").value("yyyy-mm-dd 포맷")),
                    fieldWithPath("roomList[].endDate").type(JsonFieldType.STRING)
                        .description("예약 종료일")
                        .attributes(key("constraints").value("yyyy-mm-dd 포맷")),
                    fieldWithPath("roomList[].price").type(JsonFieldType.NUMBER).description("가격"),
                    fieldWithPath("roomList[].personnel").type(JsonFieldType.NUMBER).description("예약 인원")
                        .attributes(key("constraints").value("1명 이상")),
                    fieldWithPath("agreement").type(JsonFieldType.BOOLEAN).description("약관 동의 여부")
                        .attributes(key("constraints").value("반드시 True여야 함.")),
                    fieldWithPath("fromCart").type(JsonFieldType.BOOLEAN)
                        .description("문서화 오류입니다. 이 필드는 제외해주세요.")
                        .attributes(key("constraints").value("문서화 오류입니다. 이 필드는 제외해주세요.")),
                    fieldWithPath("isFromCart").type(JsonFieldType.BOOLEAN)
                        .description("카트에서 주문했는지에 대한 여부")
                        .attributes(key("constraints").value("Not Null"))),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"),
                    fieldWithPath("data.roomList[]").type(JsonFieldType.ARRAY)
                        .description("예약된 방 정보"),
                    fieldWithPath("data.roomList[].roomId").type(JsonFieldType.NUMBER)
                        .description("객실 식별자(ID)"),
                    fieldWithPath("data.roomList[].type").type(JsonFieldType.STRING)
                        .description("객실 타입"),
                    fieldWithPath("data.roomList[].checkIn").type(JsonFieldType.STRING)
                        .description("체크인 시간"),
                    fieldWithPath("data.roomList[].checkOut").type(JsonFieldType.STRING)
                        .description("체크아웃 시간"),
                    fieldWithPath("data.roomList[].capacity").type(JsonFieldType.NUMBER)
                        .description("기준 인원"),
                    fieldWithPath("data.roomList[].maxCapacity").type(JsonFieldType.NUMBER)
                        .description("최대 인원"),
                    fieldWithPath("data.roomList[].price").type(JsonFieldType.NUMBER)
                        .description("결제 가격"),
                    fieldWithPath("data.roomList[].startDate").type(JsonFieldType.STRING)
                        .description("해당 방의 예약 시작일"),
                    fieldWithPath("data.roomList[].endDate").type(JsonFieldType.STRING)
                        .description("해당 방의 예약 종료일"),
                    fieldWithPath("data.roomList[].roomImageUrl").type(JsonFieldType.STRING)
                        .optional().description("방의 이미지"),
                    fieldWithPath("data.roomList[].roomReservationNumber").type(
                        JsonFieldType.NUMBER).optional().description("방 예약번호"),
                    fieldWithPath("data.reservationNumber").type(JsonFieldType.NUMBER)
                        .optional().description("주문번호"),
                    fieldWithPath("data.dealDateTime").type(JsonFieldType.STRING).optional()
                        .description("거래 일시")
                )));
    }
}

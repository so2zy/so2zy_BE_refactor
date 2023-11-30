package com.aroom.domain.accommodation.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.controller.AccommodationRestController;
import com.aroom.domain.accommodation.service.AccommodationService;
import com.aroom.global.config.CustomHttpHeaders;
import com.aroom.util.docs.RestDocsHelper;
import com.aroom.util.fixture.AccommodationResponseFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@Disabled
class AccommodationControllerDocsTest extends RestDocsHelper {

    private AccommodationService accommodationService = mock(AccommodationService.class);

    @Override
    public Object initController() {
        return new AccommodationRestController(accommodationService);
    }

    @DisplayName("숙소 목록 검색 문서화")
    @Test
    void searchAccommodationListDocumentation_willSuccess() throws Exception {
        //when then
        mockMvc.perform(get("/v2/accommodations", 1)
                .header(CustomHttpHeaders.ACCESS_TOKEN, "JWT Access Token")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("accommodations-list-search",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("accommodationId").description("좋아요 대상 숙소 ID")
                        .attributes(key("constraints").value("Not Null"))),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"))));
    }

    @DisplayName("숙소 상세 조회 문서화")
    @Test
    void searchAccommodationDetailsDocumentation_willSuccess() throws Exception {
        //when then

        when(accommodationService.getRoom(any(), any(), any(), any(), any()))
            .thenReturn(AccommodationResponseFixture
                .getAccommodationDetailFixture(AccommodationResponseFixture.getRoomListResponse()));

        mockMvc.perform(get("/v2/accommodations/{accommodation_id}", 1)
                .header(CustomHttpHeaders.ACCESS_TOKEN, "JWT Access Token")
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(1).toString())
                .queryParam("personnel", "2")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("accommodations-detail-search",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("accommodation_id").description("상세 조회 할 숙소의 식별자(ID)")
                        .attributes(key("constraints").value("Not Null"))),
                queryParameters(
                    parameterWithName("startDate").description("예약 시작 날짜")
                        .attributes(key("constraints").value("Not Null")),
                    parameterWithName("endDate").description("예약 종료 날짜")
                        .attributes(key("constraints").value("Not Null")),
                    parameterWithName("personnel").description("숙박 인원")
                        .attributes(key("constraints").value("Not Null"))
                ),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("숙소 식별자"),
                    fieldWithPath("data.accommodationName").type(JsonFieldType.STRING)
                        .description("숙소 식별자"),
                    fieldWithPath("data.latitude").type(JsonFieldType.NUMBER).description("숙소 식별자"),
                    fieldWithPath("data.longitude").type(JsonFieldType.NUMBER)
                        .description("숙소 식별자"),
                    fieldWithPath("data.addressCode").type(JsonFieldType.STRING)
                        .description("숙소 식별자"),
                    fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING)
                        .description("숙소 식별자"),
                    fieldWithPath("data.accommodationUrl").type(JsonFieldType.STRING)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[]").type(JsonFieldType.ARRAY)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].id").type(JsonFieldType.NUMBER)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].type").type(JsonFieldType.STRING)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].price").type(JsonFieldType.NUMBER)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].capacity").type(JsonFieldType.NUMBER)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].maxCapacity").type(JsonFieldType.NUMBER)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].checkIn").type(JsonFieldType.STRING)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].checkOut").type(JsonFieldType.STRING)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].soldCount").type(JsonFieldType.NUMBER)
                        .description("숙소 식별자"),
                    fieldWithPath("data.roomInfoList[].url").type(JsonFieldType.STRING)
                        .description("숙소 식별자")
                )));
    }
}

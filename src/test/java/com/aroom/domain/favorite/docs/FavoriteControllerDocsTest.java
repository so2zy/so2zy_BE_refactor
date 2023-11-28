package com.aroom.domain.favorite.docs;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.global.config.CustomHttpHeaders;
import com.aroom.util.docs.RestDocsHelper;
import com.aroom.domain.favorite.controller.FavoriteController;
import com.aroom.domain.favorite.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class FavoriteControllerDocsTest extends RestDocsHelper {

    private final FavoriteService favoriteService = mock(FavoriteService.class);

    @Override
    public Object initController() {
        return new FavoriteController(favoriteService);
    }

    @DisplayName("찜하기 문서화")
    @Test
    void createFavoriteDocumentation_willSuccess() throws Exception {
        //when then
        mockMvc.perform(post("/v1/accommodations/{accommodationId}/favorite", 1)
                .header(CustomHttpHeaders.ACCESS_TOKEN,
                    "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLWtleSI6IjEiLCJ1c2VyLW5hbWUiOiLtmozsm5DsnbTrpoQiLCJpc3MiOiJBcm9vbS1CRSIsImlhdCI6MTcwMTE1MTU2NiwiZXhwIjoxNzAxMTg3NTY2fQ.JG1IvSgY830E6tHfzXlvyruTwLIuE6LLzqGOldLoKPEbbF70BquHY_7h9P2JI78AdZLXIvOoUToDpxGI49H0EA")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("favorite-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("accommodationId").description("찜할 숙소 식별자(ID)")
                        .attributes(key("constraints").value("Not Null"))),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"))));
    }

    @DisplayName("찜하기 취소 문서화")
    @Test
    void deleteFavoriteDocumentation_willSuccess() throws Exception {
        //when then
        mockMvc.perform(delete("/v1/accommodations/{accommodationId}/favorite", 1)
                .header(CustomHttpHeaders.ACCESS_TOKEN,
                    "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLWtleSI6IjEiLCJ1c2VyLW5hbWUiOiLtmozsm5DsnbTrpoQiLCJpc3MiOiJBcm9vbS1CRSIsImlhdCI6MTcwMTE1MTU2NiwiZXhwIjoxNzAxMTg3NTY2fQ.JG1IvSgY830E6tHfzXlvyruTwLIuE6LLzqGOldLoKPEbbF70BquHY_7h9P2JI78AdZLXIvOoUToDpxGI49H0EA")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("favorite-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("accommodationId").description("찜에서 제외 할 숙소 식별자(ID)")
                        .attributes(key("constraints").value("Not Null"))),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"))));
    }
}

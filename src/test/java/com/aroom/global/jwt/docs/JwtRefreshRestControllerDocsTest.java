package com.aroom.global.jwt.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.global.jwt.controller.JwtRefreshRestController;
import com.aroom.global.jwt.controller.RefreshAccessTokenRequest;
import com.aroom.global.jwt.service.JwtService;
import com.aroom.global.jwt.service.TokenResponse;
import com.aroom.util.docs.RestDocsHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

class JwtRefreshRestControllerDocsTest extends RestDocsHelper {

    private final JwtService jwtService = mock(JwtService.class);

    @Override
    public Object initController() {
        return new JwtRefreshRestController(jwtService);
    }

    @DisplayName("찜하기 문서화")
    @Test
    void createFavoriteDocumentation_willSuccess() throws Exception {
        // given
        RefreshAccessTokenRequest request = new RefreshAccessTokenRequest(
            "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLWtleSI6IjEiLCJ1c2VyLW5hbWUiOiLtmozsm5DsnbTrpoQiLCJpc3MiOiJBcm9vbS1CRSIsImlhdCI6MTcwMTE1MTU2NiwiZXhwIjoxNzAxMTg3NTY2fQ.JG1IvSgY830E6tHfzXlvyruTwLIuE6LLzqGOldLoKPEbbF70BquHY_7h9P2JI78AdZLXIvOoUToDpxGI49H0EA\n",
            "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLWtleSI6IjEiLCJ1c2VyLW5hbWUiOiLtmozsm5DsnbTrpoQiLCJpc3MiOiJBcm9vbS1CRSIsImlhdCI6MTcwMTE1MTU2NiwiZXhwIjoxNzAxMTg3NTY2fQ.JG1IvSgY830E6tHfzXlvyruTwLIuE6LLzqGOldLoKPEbbF70BquHY_7h9P2JI78AdZLXIvOoUToDpxGI49H0EA\n");

        when(jwtService.refreshAccessToken(any(RefreshAccessTokenRequest.class)))
            .thenReturn(new TokenResponse("new AccessToken", "new RefreshToken"));

        //when then
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/v1/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("jwt-accesstoken-refresh",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("accessToken").description("재발급 할 accessToken")
                        .attributes(key("constraints").value("Not Null")),
                    fieldWithPath("refreshToken").description("재발급에 필요한 refreshToken")
                        .attributes(key("constraints").value("Not Null"))),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                        .description("새롭게 발급된 AccessToken"),
                    fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                        .description("새롭게 발급된 RefreshToken"))));
    }
}

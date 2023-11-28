package com.aroom.domain.member.docs;


import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.util.docs.RestDocsHelper;
import com.aroom.domain.member.controller.MemberController;
import com.aroom.domain.member.dto.request.MemberRegisterRequest;
import com.aroom.domain.member.service.MemberRegisterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;


class RegisterControllerDocsTest extends RestDocsHelper {

    private final MemberRegisterService memberRegisterService = mock(MemberRegisterService.class);

    private static final String MEMBER_API_PREFIX = "/v1/members";

    @Override
    public Object initController() {
        return new MemberController(memberRegisterService);
    }

    @DisplayName("회원가입 API 문서화")
    @Test
    void registerDocumentation_willSuccess() throws Exception {
        //given
        MemberRegisterRequest request = new MemberRegisterRequest("test@email.com", "testPassword",
            "닉네임1");

        //when then
        mockMvc.perform(post(MEMBER_API_PREFIX + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("member-register",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일(아이디)")
                        .attributes(key("constraints").value("Not Blank")),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        .attributes(key("constraints").value("Not Blank")),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("닉네임")
                        .attributes(key("constraints").value("Not Blank"))),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"))));
    }

    @DisplayName("이메일 인증 문서화")
    @Test
    void loginDocumentation_willSuccess() throws Exception {
        //when then
        mockMvc.perform(get(MEMBER_API_PREFIX + "/email/verify")
                .queryParam("email", "test@email.com"))
            .andExpect(status().isOk())
            .andDo(document("member-email-verify",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("email").description("중복여부를 검사할 이메일")
                        .attributes(key("constraints").value("이메일 포맷이어야 합니다."))
                ),
                responseFields(
                    fieldWithPath("timeStamp").type(JsonFieldType.STRING).description("응답 시간"),
                    fieldWithPath("detail").type(JsonFieldType.STRING).optional()
                        .description("응답 상세 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional()
                        .description("응답 데이터"))));
    }
}

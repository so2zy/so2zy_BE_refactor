package com.aroom.domain.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.member.dto.request.MemberRegisterRequest;
import com.aroom.util.ControllerTestWithoutSecurityHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;


public class MemberControllerTest extends ControllerTestWithoutSecurityHelper {

    @DisplayName("회원가입은")
    @Nested
    class Context_register {
        @DisplayName("적절한 값을 입력하면 성공한다.")
        @Test
        void willSuccess() throws Exception {

            // given
            MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest("test@email.com", "password1", "nickname");

            // when then
            mockMvc.perform(post("/v1/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(memberRegisterRequest)))
                .andExpect(status().isOk());
        }

        @DisplayName("이메일 형식이 아닌 경우 실패한다.")
        @Test
        void email_isNotAcceptableFormat_400_willReturn() throws Exception {

            // given
            MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest("notEmail", "password1", "nickname");

            // when then
            mockMvc.perform(post("/v1/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(memberRegisterRequest)))
                .andExpect(status().isBadRequest());
        }

        @DisplayName("각각의 값들이 누락이거나 공란이라면 실패한다.")
        @CsvSource(value = {
            ",password1,nickname", "   ,password1,nickname",
            "test@email.com,,nickname", "test@email.com,   ,nickname",
            "test@email.com,password,", "test@email.com,password,   "})
        @ParameterizedTest
        void eachParameter_isBlankOrNull_400_willReturn(String email, String password, String nickname) throws Exception {

            // given
            MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest(email, password, nickname);

            // when then
            mockMvc.perform(post("/v1/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(memberRegisterRequest)))
                .andExpect(status().isBadRequest());
        }
    }
}

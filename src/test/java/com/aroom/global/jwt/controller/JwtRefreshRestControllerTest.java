package com.aroom.global.jwt.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.util.ControllerTestWithoutSecurityHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class JwtRefreshRestControllerTest extends ControllerTestWithoutSecurityHelper {

    @DisplayName("accessToken이 비어있으면 실패한다.")
    @Test
    void accessTokenIsBlank_willFail() throws Exception {
        mockMvc.perform(post("/v1/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(new RefreshAccessTokenRequest(null, "testToken"))))
            .andExpect(status().isBadRequest());

        verify(jwtService, never()).refreshAccessToken(any(RefreshAccessTokenRequest.class));
    }

    @DisplayName("refreshToken이 비어있으면 실패한다.")
    @Test
    void refreshTokenIsBlank_willFail() throws Exception {
        mockMvc.perform(post("/v1/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(new RefreshAccessTokenRequest("testToken", null))))
            .andExpect(status().isBadRequest());

        verify(jwtService, never()).refreshAccessToken(any(RefreshAccessTokenRequest.class));
    }

}

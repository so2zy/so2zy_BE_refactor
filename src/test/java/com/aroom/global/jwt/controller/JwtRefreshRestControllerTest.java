package com.aroom.global.jwt.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.global.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = JwtRefreshRestController.class)
class JwtRefreshRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

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

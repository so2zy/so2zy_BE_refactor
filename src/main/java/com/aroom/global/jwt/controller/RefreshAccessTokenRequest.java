package com.aroom.global.jwt.controller;

import jakarta.validation.constraints.NotBlank;

public record RefreshAccessTokenRequest(
    @NotBlank String accessToken,
    @NotBlank String refreshToken
) {

}

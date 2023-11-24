package com.aroom.global.jwt.service;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {

}

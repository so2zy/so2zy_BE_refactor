package com.aroom.global.jwt.model;

public record JwtEntity(
    String key,
    String value,
    long expiration
) {
}

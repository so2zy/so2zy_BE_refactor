package com.aroom.global.jwt.dto;

import java.util.Date;

public record JwtCreateRequest(
    Long memberId,
    String email,
    Date issuedAt
) {
}

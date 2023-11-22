package com.aroom.global.jwt;

import java.util.Date;

public record JwtPayload(
    String email,
    Date issuedAt
) {

}

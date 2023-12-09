package com.fastcampus.mini9.domain.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfoResponseDto(
    @Schema(example = "donghar@naver.com")
    String email,
    String name,
    @Schema(example = "2023-01-01")
    String birthday
) {

}

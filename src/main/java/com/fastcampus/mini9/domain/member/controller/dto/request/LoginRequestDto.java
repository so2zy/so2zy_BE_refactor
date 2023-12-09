package com.fastcampus.mini9.domain.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
    @Schema(example = "donghar@naver.com")
    String email,
    @Schema(example = "12345678")
    String pwd
) {

}

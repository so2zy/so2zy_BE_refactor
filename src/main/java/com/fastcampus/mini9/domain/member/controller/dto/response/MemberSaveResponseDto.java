package com.fastcampus.mini9.domain.member.controller.dto.response;

import java.time.LocalDate;

public record MemberSaveResponseDto(
    Long id,
    String email,
    String name,
    LocalDate birthday
) {

}

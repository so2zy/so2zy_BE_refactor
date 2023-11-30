package com.aroom.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRegisterRequest(
    @Email @NotBlank String email,
    @NotBlank String password,
    @NotBlank String name
) {

}

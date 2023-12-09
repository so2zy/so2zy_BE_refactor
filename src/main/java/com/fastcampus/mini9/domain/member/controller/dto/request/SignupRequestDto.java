package com.fastcampus.mini9.domain.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record SignupRequestDto(
    @Schema(example = "donghar@naver.com")
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    String email,

    @NotBlank(message = "패스워드는 필수 입력 사항입니다.")
    @Size(min = 8, message = "8자리 이상의 비밀번호를 입력해 주세요")
    @Schema(example = "12345678")
    String pwd,

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    String name,

    LocalDate birthday
) {

}

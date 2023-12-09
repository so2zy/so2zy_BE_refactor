package com.fastcampus.mini9.domain.member.service.dto.request;

import com.fastcampus.mini9.domain.member.entity.Member;
import java.time.LocalDate;

public record MemberSaveDto(
    String email,
    String pwd,
    String name,
    LocalDate birthday
) {

    public Member toEntity(String encodePassword) {
        return Member.builder()
            .email(email)
            .pwd(encodePassword)
            .name(name)
            .birthday(birthday)
            .build();
    }
}

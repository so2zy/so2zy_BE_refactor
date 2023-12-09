package com.fastcampus.mini9.domain.member.service.dto.response;

import com.fastcampus.mini9.domain.member.entity.Member;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record MemberDto(
    Long id,
    String email,
    String name,
    LocalDate birthday
) {

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .birthday(member.getBirthday())
            .build();
    }
}

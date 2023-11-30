package com.aroom.domain.member.service;

import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse getByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        return new MemberResponse(member.getId(), member.getEmail(), member.getName());
    }

    public record MemberResponse(
        Long memberId,
        String email,
        String name
    ) {
    }
}

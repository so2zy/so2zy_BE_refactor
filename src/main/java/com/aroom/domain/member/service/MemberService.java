package com.aroom.domain.member.service;

import com.aroom.domain.member.dto.request.SignUpRequest;
import com.aroom.domain.member.exception.MemberEmailDuplicateException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(SignUpRequest request) {
        memberRepository.save(Member.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .name(request.name())
            .build());
    }

    public void validateEmailDuplicatation(String email) {
        if (memberRepository.findMemberByEmail(email).isPresent()) {
            throw new MemberEmailDuplicateException();
        }
    }
}

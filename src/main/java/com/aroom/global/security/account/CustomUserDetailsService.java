package com.aroom.global.security.account;

import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username: {}", username);
        List<Member> all = memberRepository.findAll();
        log.info("memberList: {}", all);
        Member member = memberRepository.findMemberByEmail(username)
            .orElseThrow(MemberNotFoundException::new);

        return new AccountContext(member.getId(), member.getEmail(), member.getPassword(),
            Set.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}

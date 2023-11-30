package com.aroom.global.basetime;

import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.global.config.JpaAuditingConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(JpaAuditingConfig.class)
@DataJpaTest
class BaseTimeEntityTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("엔티티 save 시 생성일이 저장됩니다.")
    void save_created_at_success() {
        // given
        Member tester1 = Member.builder()
            .name("tester1")
            .email("qwe@123")
            .password("15fs@#1")
            .build();

        Member tester2 = Member.builder()
            .name("tester2")
            .email("abcde@ads")
            .password("ggasd12")
            .build();

        // when
        Member beforeSavedMember = memberRepository.save(tester1);
        Member afterSavedMember = memberRepository.save(tester2);

        // then
        Assertions.assertThat(beforeSavedMember.getCreatedAt()).isBefore(afterSavedMember.getCreatedAt());
    }
}
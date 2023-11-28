package com.aroom.domain.member.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.aroom.domain.member.exception.MemberEmailDuplicateException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class MemberRegisterServiceTest {

    @InjectMocks
    private MemberRegisterService memberRegisterService;

    @Mock
    private MemberRepository memberRepository;

    @DisplayName("validateEmailDuplicatation() 은")
    @Nested
    class Context_validateEmailDuplicatation {

        @DisplayName("중복되는 이메일이 없는 경우 인증에 성공한다.")
        @Test
        void thereIsNoEmail_thatDuplicated_willSuccess() {

            // given
            String targetEmail = "test@email.com";
            given(memberRepository.findMemberByEmail(anyString())).willReturn(Optional.empty());

            // when then
            assertThatCode(() -> memberRegisterService.validateEmailDuplicatation(targetEmail))
                .doesNotThrowAnyException();
            verify(memberRepository, times(1)).findMemberByEmail(anyString());
        }

        @DisplayName("중복된는 경우 MemberEmailDuplicateException 예외가 발생한다.")
        @Test
        void emailDuplicated_MemberEmailDuplicateException_willThrown() {

            // given
            String targetEmail = "test@email.com";
            given(memberRepository.findMemberByEmail(anyString()))
                .willReturn(Optional.of(Member.builder().build()));

            // when then
            assertThatThrownBy(() -> memberRegisterService.validateEmailDuplicatation(targetEmail))
                .isInstanceOf(MemberEmailDuplicateException.class);
            verify(memberRepository, times(1)).findMemberByEmail(anyString());
        }
    }
}

package com.aroom.domain.reservation.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.reservation.model.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member tester;
    @BeforeEach
    private void init(){
        tester = Member.builder()
            .name("tester")
            .email("test@test.com")
            .password("test123@!")
            .build();
    }

    @Test
    @DisplayName("예약 저장 성공")
    void reservation_save_success(){
        // given
        Reservation reservation = Reservation.builder()
            .member(tester)
            .agreement(true)
            .build();

        // when
        memberRepository.save(tester);
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        Assertions.assertThat(savedReservation.getId()).isEqualTo(reservation.getId());
    }
}
package com.aroom.domain.reservation.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.aroom.domain.member.model.Member;
import com.aroom.domain.reservation.model.Reservation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

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
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        Assertions.assertThat(savedReservation.getId()).isEqualTo(reservation.getId());
    }
}
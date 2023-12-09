package com.fastcampus.mini9.domain.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.mini9.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}

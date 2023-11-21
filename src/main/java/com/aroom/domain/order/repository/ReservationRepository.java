package com.aroom.domain.order.repository;

import com.aroom.domain.order.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}

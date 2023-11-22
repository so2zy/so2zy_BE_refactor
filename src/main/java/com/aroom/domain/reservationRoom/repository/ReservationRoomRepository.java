package com.aroom.domain.reservationRoom.repository;

import com.aroom.domain.reservationRoom.model.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

}

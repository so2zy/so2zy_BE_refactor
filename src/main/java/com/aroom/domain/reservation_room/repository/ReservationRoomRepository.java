package com.aroom.domain.reservation_room.repository;

import com.aroom.domain.reservation_room.model.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

}

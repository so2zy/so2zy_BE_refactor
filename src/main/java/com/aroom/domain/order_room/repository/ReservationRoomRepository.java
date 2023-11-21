package com.aroom.domain.order_room.repository;

import com.aroom.domain.order_room.model.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

}

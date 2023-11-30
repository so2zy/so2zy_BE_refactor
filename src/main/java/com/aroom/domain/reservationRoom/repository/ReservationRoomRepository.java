package com.aroom.domain.reservationRoom.repository;

import com.aroom.domain.reservationRoom.model.ReservationRoom;
import com.aroom.domain.room.model.Room;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

}

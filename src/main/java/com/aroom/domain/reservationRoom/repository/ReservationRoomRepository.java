package com.aroom.domain.reservationRoom.repository;

import com.aroom.domain.reservationRoom.model.ReservationRoom;
import com.aroom.domain.room.model.Room;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

    @Query("select count(r) from ReservationRoom r where r.room = :room and "
        + "((r.startDate between :startDate and :endDate) or (r.endDate between :startDate and :endDate))")
    int getOverlappingReservationByDateRange(@Param("room") Room room,
        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

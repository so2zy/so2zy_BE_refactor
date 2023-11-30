package com.aroom.domain.roomProduct.repository;

import com.aroom.domain.room.model.Room;
import com.aroom.domain.roomProduct.model.RoomProduct;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomProductRepository extends JpaRepository<RoomProduct, Long> {

    @Query("select rp from RoomProduct rp where rp.room.id = :roomId and rp.startDate =:startDate and rp.stock > 0")
    Optional<RoomProduct> findByRoomIdAndStartDate(@Param("roomId") long roomId,
        @Param("startDate") LocalDate startDate);

    @Query("select rp from RoomProduct rp where rp.room.id = :roomId and (rp.startDate between :startDate and :endDate) and rp.stock > 0")
    List<RoomProduct> findByRoomIdAndStartDateAndEndDate(@Param("roomId") long roomId,
        @Param("startDate")
            LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select rp from RoomProduct rp where rp.room = :room and (rp.startDate between :startDate and :endDate)")
    List<RoomProduct> findByRoomAndBetweenStartDateAndEndDate(@Param("room") Room room,
        @Param("startDate")
            LocalDate startDate, @Param("endDate") LocalDate endDate);
}

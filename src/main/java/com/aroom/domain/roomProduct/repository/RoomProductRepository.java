package com.aroom.domain.roomProduct.repository;

import com.aroom.domain.room.model.Room;
import com.aroom.domain.roomProduct.model.RoomProduct;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomProductRepository extends JpaRepository<RoomProduct, Long> {

    Optional<RoomProduct> findByRoomId(Long roomId);

    @Query("select rp from RoomProduct rp where rp.room = :room and (rp.startDate between :startDate and :endDate)")
    List<RoomProduct> findByRoomAndBetweenStartDateAndEndDate(@Param("room")Room room, @Param("startDate")
        LocalDate startDate, @Param("endDate") LocalDate endDate);

}
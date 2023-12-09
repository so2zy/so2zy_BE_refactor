package com.fastcampus.mini9.domain.accommodation.repository;

import com.fastcampus.mini9.domain.accommodation.entity.room.Room;
import com.fastcampus.mini9.domain.accommodation.entity.room.Stock;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    boolean existsByRoomAndDate(Room room, LocalDate date);

    @Modifying
    @Query("DELETE FROM Stock s WHERE s.date < :date")
    void deleteStocksBeforeDate(@Param("date") LocalDate date);
}

package com.fastcampus.mini9.common.util.schduler;

import com.fastcampus.mini9.domain.accommodation.entity.room.Room;
import com.fastcampus.mini9.domain.accommodation.entity.room.Stock;
import com.fastcampus.mini9.domain.accommodation.repository.RoomRepository;
import com.fastcampus.mini9.domain.accommodation.repository.StockRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StockService {

    private final RoomRepository roomRepository;

    private final StockRepository stockRepository;

    @Async
    public void createStocks(LocalDate startDate, LocalDate endDate) {
        List<Room> rooms = roomRepository.findAll();
        rooms.parallelStream().forEach(room ->
            startDate.datesUntil(endDate).forEach(date ->
                saveStock(room, date)));
    }

    @Async
    public void saveStock(Room room, LocalDate date) {
        Stock stock = Stock.builder()
            .room(room)
            .date(date)
            .quantity(room.getNumberOfRoom())
            .build();
        stockRepository.save(stock);
    }

    @Async
    public void deleteBeforeStock(LocalDate date) {
        stockRepository.deleteStocksBeforeDate(date);
    }
}

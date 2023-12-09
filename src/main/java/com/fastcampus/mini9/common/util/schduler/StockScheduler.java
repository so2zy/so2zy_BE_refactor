package com.fastcampus.mini9.common.util.schduler;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockScheduler {

    private static final int INIT_MONTH = 2;

    private final StockService stockService;

    @Async
    @Scheduled(cron = "00 00 00 * * *")
    public void dailyStock() {
        LocalDate startDate = LocalDate.now().plusMonths(INIT_MONTH);
        stockService.createStocks(startDate, startDate.plusDays(1));
        stockService.deleteBeforeStock(LocalDate.now());
    }
}

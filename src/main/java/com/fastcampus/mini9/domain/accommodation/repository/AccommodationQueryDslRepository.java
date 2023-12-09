package com.fastcampus.mini9.domain.accommodation.repository;

import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.accommodation.exception.repository.IllegalRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AccommodationQueryDslRepository {
    /**
     * 1.해당 지역에
     * 2.해당 날짜에 재고가 있는 숙소 중
     * 3.해당 카테고리, 키워드에 해당하는 값들
     * 4.details정보까지 fetchJoin해서
     * 5.페이지네이션
     * TODO: isStock, isWish를 가진 dto를 반환하게 변경
     * @param region
     * @param district
     * @param startDate
     * @param endDate
     * @param category
     * @param keyword
     * @param of
     * @return
     */
    Page<Accommodation> searchByConditions(
            String region, String district,
            LocalDate startDate, LocalDate endDate,
            String category, String keyword,
            Pageable of
    ) throws IllegalRequestException;
}

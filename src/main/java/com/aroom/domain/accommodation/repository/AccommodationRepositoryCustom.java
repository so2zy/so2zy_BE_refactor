package com.aroom.domain.accommodation.repository;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepositoryCustom {
    Page<AccommodationListResponse.InnerClass> getAll(Pageable pageable);

    Page<AccommodationListResponse.InnerClass> searchByCondition(SearchCondition searchCondition,
        Pageable pageable);

    Page<AccommodationListResponse.InnerClass> searchByConditionWithSort(SearchCondition searchCondition,
        Pageable pageable);
}

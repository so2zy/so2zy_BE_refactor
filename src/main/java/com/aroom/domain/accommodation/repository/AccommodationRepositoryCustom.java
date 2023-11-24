package com.aroom.domain.accommodation.repository;

import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.model.Accommodation;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepositoryCustom {

    List<Accommodation> getAccommodationBySearchCondition(SearchCondition searchCondition, Pageable pageable);

    List<Accommodation> getAccommodationBySearchConditionWithSortCondition(SearchCondition searchCondition, Pageable pageable, Sort sortCondition);
}

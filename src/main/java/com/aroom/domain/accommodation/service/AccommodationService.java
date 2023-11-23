package com.aroom.domain.accommodation.service;

import static com.aroom.domain.accommodation.controller.AccommodationRestController.NO_ORDER_CONDITION;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public List<AccommodationListResponse> getAccommodationListBySearchCondition(
        SearchCondition searchCondition, Pageable pageable, Sort sortCondition) {
        if (sortCondition.toString().contains(NO_ORDER_CONDITION)){
            List<Accommodation> accommodation = accommodationRepository.getAccommodationBySearchCondition(
                searchCondition, pageable);
            return accommodation.stream()
                .map(AccommodationListResponse::fromEntity)
                .toList();
        }

        List<Accommodation> accommodation = accommodationRepository.getAccommodationBySearchConditionWithSortCondition(
            searchCondition, pageable, sortCondition);
        return accommodation.stream()
            .map(AccommodationListResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<AccommodationListResponse> getAllAccommodation() {
        List<Accommodation> accommodation = accommodationRepository.findAll();

        return accommodation.stream()
            .map(AccommodationListResponse::fromEntity)
            .toList();
    }
}

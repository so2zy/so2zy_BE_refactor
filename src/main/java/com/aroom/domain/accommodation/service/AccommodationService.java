package com.aroom.domain.accommodation.service;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public List<AccommodationListResponse> getAccommodationListBySearchCondition(
        SearchCondition searchCondition, Pageable pageable) {
        List<Accommodation> accommodation = accommodationRepository.getAccommodationBySearchCondition(
            searchCondition, pageable);
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

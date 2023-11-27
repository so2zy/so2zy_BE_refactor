package com.aroom.domain.accommodation.service;

import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import static com.aroom.domain.accommodation.controller.AccommodationRestController.NO_ORDER_CONDITION;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public AccommodationResponse getRoom(Long accommodation_id) {
        Accommodation accommodation = accommodationRepository.findById(accommodation_id)
            .orElseThrow(
                AccommodationNotFoundException::new);
        return new AccommodationResponse(accommodation);
    }

    @Transactional(readOnly = true)
    public List<AccommodationListResponse> getAccommodationListBySearchCondition(
        SearchCondition searchCondition, Pageable pageable, Sort sortCondition) {

        if (sortCondition.toString().contains(NO_ORDER_CONDITION)){

            List<Accommodation> accommodation = accommodationRepository.getAccommodationBySearchCondition(
                searchCondition, pageable);
            Integer page = pageable.getPageNumber();
            Integer size = pageable.getPageSize();

            return accommodation.stream()
                .map(entity -> AccommodationListResponse.fromEntity(entity, page, size))
                .toList();
        }

        List<Accommodation> accommodation = accommodationRepository.getAccommodationBySearchConditionWithSortCondition(
            searchCondition, pageable, sortCondition);
        Integer page = pageable.getPageNumber();
        Integer size = pageable.getPageSize();
        return accommodation.stream()
            .map(entity -> AccommodationListResponse.fromEntity(entity, page, size))
            .toList();
    }
    @Transactional(readOnly = true)
    public List<AccommodationListResponse> getAccommodationListByDateSearchCondition(
        SearchCondition searchCondition,
        Pageable pageable,
        Sort sortCondition
    ){
        if (sortCondition.toString().contains(NO_ORDER_CONDITION)) {
            Integer page = pageable.getPageNumber();
            Integer size = pageable.getPageSize();
            return accommodationRepository.getAccommodationByDateSearchCondition(searchCondition,
                    pageable)
                .stream()
                .map(entity -> AccommodationListResponse.fromEntity(entity, page, size))
                .toList();
        }
        Integer page = pageable.getPageNumber();
        Integer size = pageable.getPageSize();
        return accommodationRepository.getAccommodationByDateSearchConditionWithSortCondition(searchCondition,
                pageable, sortCondition)
            .stream()
            .map(entity -> AccommodationListResponse.fromEntity(entity, page, size))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<AccommodationListResponse> getAllAccommodation() {
        List<Accommodation> accommodation = accommodationRepository.findAll();

        return accommodation.stream()
            .map(entity -> AccommodationListResponse.fromEntity(entity, null, null))
            .toList();
    }
}

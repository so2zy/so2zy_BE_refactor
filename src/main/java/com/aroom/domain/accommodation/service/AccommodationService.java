package com.aroom.domain.accommodation.service;

import static com.aroom.domain.accommodation.controller.AccommodationRestController.NO_ORDER_CONDITION;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.AccommodationListResponse.InnerClass;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public AccommodationListResponse getAccommodationListBySearchCondition(
        SearchCondition searchCondition, Pageable pageable, Sort sortCondition) {

        if (sortCondition.toString().contains(NO_ORDER_CONDITION)) {

            List<Accommodation> accommodation = accommodationRepository.getAccommodationBySearchCondition(
                searchCondition, pageable);

           return convertAccommodationListToAccommodationListResponse(
                pageable, accommodation);
        }

        List<Accommodation> accommodation = accommodationRepository.getAccommodationBySearchConditionWithSortCondition(
            searchCondition, pageable, sortCondition);

        return convertAccommodationListToAccommodationListResponse(
            pageable, accommodation);
    }

    @Transactional(readOnly = true)
    public AccommodationListResponse getAccommodationListByDateSearchCondition(
        SearchCondition searchCondition,
        Pageable pageable,
        Sort sortCondition
    ) {
        if (sortCondition.toString().contains(NO_ORDER_CONDITION)) {

            List<Accommodation> accommodation = accommodationRepository.getAccommodationByDateSearchCondition(
                searchCondition,
                pageable);
            return convertAccommodationListToAccommodationListResponse(
                pageable, accommodation);
        }
        List<Accommodation> accommodation = accommodationRepository.getAccommodationByDateSearchConditionWithSortCondition(
            searchCondition,
            pageable, sortCondition);
        return convertAccommodationListToAccommodationListResponse(
            pageable, accommodation);

    }

    public AccommodationListResponse convertAccommodationListToAccommodationListResponse(Pageable pageable,
        List<Accommodation> accommodation) {
        AccommodationListResponse accommodationListResponse = new AccommodationListResponse();

        accommodation.stream().map(InnerClass::fromEntity)
            .forEach(accommodationListResponse::addBody);

        accommodationListResponse.setPaginationInfo(pageable.getPageNumber(),
            pageable.getPageSize());
        return accommodationListResponse;
    }

    @Transactional(readOnly = true)
    public AccommodationListResponse getAllAccommodation(Pageable pageable) {
        List<Accommodation> accommodation = accommodationRepository.findAll();

        AccommodationListResponse accommodationListResponse = convertAccommodationListToAccommodationListResponse(
            pageable, accommodation);

        return accommodationListResponse;
    }
}




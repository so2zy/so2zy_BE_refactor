package com.aroom.domain.accommodation.service;

import static com.aroom.domain.accommodation.controller.AccommodationRestController.NO_ORDER_CONDITION;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationOnlyResponse;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    public Object getRoom(Long accommodation_id, String startDate, String endDate,
        Integer personnel, Long member_id) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateTypeChanged = LocalDate.parse(startDate, formatter);
        LocalDate endDateTypeChanged = LocalDate.parse(endDate, formatter);
        long betweenDays = ChronoUnit.DAYS.between(startDateTypeChanged, endDateTypeChanged);

        try {
            Accommodation accommodation = accommodationRepository.findByAccommodationIdAndStartDate(
                    accommodation_id, startDateTypeChanged, endDateTypeChanged)
                .orElseThrow(AccommodationNotFoundException::new);
            boolean isFavorite = accommodationRepository.findByAccommodationIdAndMemberId(
                member_id, accommodation_id).isPresent();
            return new AccommodationResponse(accommodation, personnel, isFavorite, betweenDays,
                startDateTypeChanged);
        } catch (AccommodationNotFoundException e) {
            return new AccommodationOnlyResponse(
                accommodationRepository.findById(accommodation_id).get(),
                accommodationRepository.findByAccommodationIdAndMemberId(
                    member_id, accommodation_id).isPresent());
        }
    }

    @Transactional(readOnly = true)
    public List<AccommodationListResponse> getAccommodationListBySearchCondition(
        SearchCondition searchCondition, Pageable pageable, Sort sortCondition) {
        if (sortCondition.toString().contains(NO_ORDER_CONDITION)) {
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

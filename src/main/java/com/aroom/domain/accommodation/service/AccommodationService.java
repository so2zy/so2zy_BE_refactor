package com.aroom.domain.accommodation.service;

import static com.aroom.domain.accommodation.controller.AccommodationRestController.NO_ORDER_CONDITION;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.AccommodationListResponse.InnerClass;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationOnlyResponse;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import com.aroom.domain.roomCart.dto.request.RoomCartRequest;
import com.aroom.domain.roomCart.exception.WrongDateException;
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
        checkStartDateEndDate(startDateTypeChanged,endDateTypeChanged);
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
    public AccommodationListResponse getAccommodationListBySearchCondition(
        SearchCondition searchCondition, Pageable pageable, Sort sortCondition) {
        if (searchCondition.getOrderCondition() == null && searchCondition.getOrderBy() == null){
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

        log.info("page is {}, size is {}", pageable.getPageNumber(),pageable.getPageSize());
        log.info("AccommodationListResponse Size is {}", accommodationListResponse.getBody().size());
        return accommodationListResponse;
    }

    @Transactional(readOnly = true)
    public AccommodationListResponse getAllAccommodation(Pageable pageable) {
        List<Accommodation> accommodation = accommodationRepository.getAll(pageable);

        AccommodationListResponse accommodationListResponse = convertAccommodationListToAccommodationListResponse(
            pageable, accommodation);

        return accommodationListResponse;
    }

    private void checkStartDateEndDate(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate)){
            throw new WrongDateException();
        }
    }
}




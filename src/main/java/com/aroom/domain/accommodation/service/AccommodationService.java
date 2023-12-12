package com.aroom.domain.accommodation.service;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.AccommodationListResponse.InnerClass;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationOnlyResponse;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import com.aroom.domain.roomCart.exception.WrongDateException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        checkStartDateEndDate(startDateTypeChanged, endDateTypeChanged);
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
    private void checkStartDateEndDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new WrongDateException();
        }
    }

    @Transactional(readOnly = true)
    public AccommodationListResponse findAccommodations(SearchCondition searchCondition,
        Pageable pageable) {
        if (validateQueryParamIsNull(searchCondition)) {
            return convertInnerClassToAccommodationListResponse(
                accommodationRepository.getAll(pageable)
            );
        }
        if (searchCondition.getOrderCondition() == null){
            return convertInnerClassToAccommodationListResponse(
                accommodationRepository.searchByCondition(searchCondition, pageable)
            );
        }
            return convertInnerClassToAccommodationListResponse(
                accommodationRepository.searchByConditionWithSort(searchCondition, pageable)
            );
    }

    public AccommodationListResponse convertInnerClassToAccommodationListResponse(Page<InnerClass> innerClasses) {
        if (innerClasses == null){
            throw new AccommodationNotFoundException();
        }
        AccommodationListResponse accommodationListResponse = new AccommodationListResponse();
        for (var i : innerClasses) {
            accommodationListResponse.addBody(i);
        }
        accommodationListResponse.setPaginationInfo(
            innerClasses.getNumber(),
            innerClasses.getSize(),
            innerClasses.getTotalPages());
        return accommodationListResponse;
    }

    private boolean validateQueryParamIsNull(SearchCondition searchCondition) {
        if (
            //하나라도 참이면 true 반환 즉, searchCondition의 하나라도 값이 있으면 true
            searchCondition.getOrderCondition() == null &&
                searchCondition.getHighestPrice() == null &&
                searchCondition.getLowestPrice() == null &&
                searchCondition.getCheckIn() == null &&
                searchCondition.getCheckOut() == null &&
                searchCondition.getLikeCount() == null &&
                searchCondition.getName() == null &&
                searchCondition.getPhoneNumber() == null &&
                searchCondition.getAreaName() == null &&
                searchCondition.getSigunguName() == null) {
            return true;
        }
        return false;
    }
}




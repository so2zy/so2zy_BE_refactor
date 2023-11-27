package com.aroom.domain.accommodation.controller;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.dto.response.AccommodationResponse;
import com.aroom.domain.accommodation.service.AccommodationService;
import com.aroom.global.response.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/v1/accommodations")
@RequiredArgsConstructor
public class AccommodationRestController {

    private final AccommodationService accommodationService;
    public static final String NO_ORDER_CONDITION = "default";
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<AccommodationListResponse>>> findAllAccommodation(
        @Nullable @ModelAttribute @Valid SearchCondition searchCondition,
        @Nullable @RequestParam(defaultValue = "0") Integer page,
        @Nullable @RequestParam(defaultValue = "10") Integer size
    ) {
        //쿼리스트링 유무 검증
        if (validateQueryParamIsNull(searchCondition, page, size)) {
            //Default 정렬조건 세팅
            Sort sortCondition = Sort.by(
                Direction.fromOptionalString(searchCondition.getOrderBy())
                    .orElse(Direction.ASC),
                searchCondition.getOrderCondition() == null ? NO_ORDER_CONDITION
                    : searchCondition.getOrderCondition());

            PageRequest pageRequest = PageRequest.of(page, size);

            if (searchCondition.getStartDate() !=null && searchCondition.getEndDate() != null){
                return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(LocalDateTime.now(), "숙소 정보를 성공적으로 조회했습니다",
                        accommodationService.getAccommodationListByDateSearchCondition(
                            searchCondition,pageRequest,sortCondition
                        )));
            }

            return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(LocalDateTime.now(), "숙소 정보를 성공적으로 조회했습니다.",
                    accommodationService.getAccommodationListBySearchCondition(searchCondition,
                        pageRequest, sortCondition)));
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponse<>(LocalDateTime.now(), "숙소 정보를 성공적으로 조회했습니다.",
                accommodationService.getAllAccommodation()));

    }

    private boolean validateQueryParamIsNull(SearchCondition searchCondition, Integer page, Integer size) {
        if (searchCondition.getOrderCondition()!=null ||
        searchCondition.getHighestPrice()!=null||
        searchCondition.getLowestPrice()!=null||
        searchCondition.getCheckIn()!=null||
        searchCondition.getCheckOut()!=null||
        searchCondition.getLikeCount()!=null||
        searchCondition.getName()!=null||
        searchCondition.getOrderBy()!=null||
        searchCondition.getAddressCode()!=null||
        searchCondition.getPhoneNumber()!=null||
        page!= null||
        size!= null){
            return true;
        }
        return false;
    }

    @GetMapping("/{accommodation_id}")
    public ResponseEntity<ApiResponse<AccommodationResponse>> getSpecificAccommodation(
        @PathVariable long accommodation_id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(LocalDateTime.now(), "숙소 상세 정보를 성공적으로 조회했습니다.",
                accommodationService.getRoom(accommodation_id)));
    }

}

package com.aroom.domain.accommodation.controller;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.AccommodationResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.repository.AccommodationRepositoryImpl;
import com.aroom.domain.accommodation.service.AccommodationService;
import com.aroom.global.resolver.Login;
import com.aroom.global.resolver.LoginInfo;
import com.aroom.global.response.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/v2/accommodations")
@RequiredArgsConstructor
public class AccommodationRestController {

    private final AccommodationService accommodationService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<AccommodationListResponse>> findAccommodation(
        @Nullable @ModelAttribute SearchCondition searchCondition,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable

    ) {
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize());

        return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponse<>(LocalDateTime.now(), "숙소 정보를 성공적으로 조회했습니다.",
                accommodationService.findAccommodations(searchCondition, pageRequest)));
    }


    @GetMapping("/{accommodation_id}")
    public ResponseEntity<ApiResponse<Object>> getSpecificAccommodation(
        @Login LoginInfo loginInfo,
        @PathVariable long accommodation_id, @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate, @RequestParam("personnel") int personnel) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(LocalDateTime.now(), "숙소 상세 정보를 성공적으로 조회했습니다.",
                accommodationService.getRoom(accommodation_id, startDate, endDate, personnel,
                    loginInfo.memberId())));
    }

}

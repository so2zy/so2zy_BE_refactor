package com.fastcampus.mini9.domain.accommodation.controller;

import static com.fastcampus.mini9.domain.accommodation.service.usecase.AccommodationQuery.*;
import static com.fastcampus.mini9.domain.accommodation.service.usecase.RoomQuery.*;

import java.time.LocalDate;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.mini9.common.response.DataResponseBody;
import com.fastcampus.mini9.common.response.ErrorResponseBody;
import com.fastcampus.mini9.config.security.token.UserPrincipal;
import com.fastcampus.mini9.domain.accommodation.controller.dto.AccommodationDtoMapper;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetAccommodationResponse;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetAccommodationsResponse;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetRoomResponse;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetRoomsResponse;
import com.fastcampus.mini9.domain.accommodation.service.usecase.AccommodationQuery;
import com.fastcampus.mini9.domain.accommodation.service.usecase.RoomQuery;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccommodationController {

	private final AccommodationQuery accommodationQuery;
	private final RoomQuery roomQuery;
	private final AccommodationDtoMapper mapper;

	@Operation(summary = "숙소 검색")
	@ApiResponses(value = {
		@ApiResponse(
			description = "정상적으로 호출했을 때",
			responseCode = "200"
		),
		@ApiResponse(
			description = "잘못된 형식으로 요청했을 때",
			responseCode = "400",
			content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
		),
		@ApiResponse(
			description = "서버 에러",
			responseCode = "500",
			content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
		)
	})
	@GetMapping("/accommodations")
	public DataResponseBody<GetAccommodationsResponse> getAccommodations(
		@RequestParam(required = false) String region, @RequestParam(required = false) String district,
		@RequestParam(required = false, name = "start_date") LocalDate startDate,
		@RequestParam(required = false, name = "end_date") LocalDate endDate,
		@RequestParam(required = false) String category, @RequestParam(required = false) String keyword,
		@RequestParam(name = "page_num", defaultValue = "1") Integer pageNum,
		@RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
		@AuthenticationPrincipal UserPrincipal userPrincipal) {
		SearchAccommodationsRequest searchRequest = new SearchAccommodationsRequest(region, district, startDate,
			endDate, category, keyword, pageNum, pageSize);
		SearchAccommodationsResponse searchResult = accommodationQuery.searchAccommodations(searchRequest,
			userPrincipal);
		GetAccommodationsResponse result = mapper.searchResultToResponseDto(searchResult);
		return DataResponseBody.success(result, "SUCCESS");
	}

	@Operation(summary = "숙소 상세조회")
	@ApiResponses(value = {
		@ApiResponse(
			description = "정상적으로 호출했을 때",
			responseCode = "200"
		),
		@ApiResponse(
			description = "잘못된 형식으로 요청했을 때",
			responseCode = "400",
			content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
		),
		@ApiResponse(
			description = "서버 에러",
			responseCode = "500",
			content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
		)
	})
	@GetMapping("/accommodations/{accommodationId}")
	public DataResponseBody<GetAccommodationResponse> getAccommodation(@PathVariable Long accommodationId) {
		FindAccommodationRequest findRequest = new FindAccommodationRequest(accommodationId);
		AccommodationQuery.FindAccommodationResponse findResult = accommodationQuery.findAccommodation(findRequest);
		GetAccommodationResponse result = mapper.findResultToDto(findResult);
		return DataResponseBody.success(result, "SUCCESS");
	}

	@Operation(summary = "숙소에서 제공하는 객실 조회")
	@ApiResponses(value = {
		@ApiResponse(
			description = "정상적으로 호출했을 때",
			responseCode = "200"
		),
		@ApiResponse(
			description = "잘못된 형식으로 요청했을 때",
			responseCode = "400",
			content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
		),
		@ApiResponse(
			description = "서버 에러",
			responseCode = "500",
			content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
		)
	})
	@GetMapping("/accommodations/{accommodationId}/rooms")
	public DataResponseBody<GetRoomsResponse> getRooms(@PathVariable Long accommodationId,
		@RequestParam(name = "start_date") LocalDate startDate, @RequestParam(name = "end_date") LocalDate endDate,
		@RequestParam(name = "guest_num") Long guestNum) {
		FindRoomsInAccommodationRequest findRequest = new FindRoomsInAccommodationRequest(accommodationId, startDate,
			endDate, guestNum);
		FindRoomsInAccommodationResponse findResult = roomQuery.findRoomsInAccommodation(findRequest);
		GetRoomsResponse result = mapper.findResultToDto(findResult);
		return DataResponseBody.success(result, "SUCCESS");
	}

	@Deprecated
	@Operation(summary = "객실 상세조회")
	@ApiResponses(value = {
		@ApiResponse(
			description = "정상적으로 호출했을 때",
			responseCode = "200"
		),
		@ApiResponse(
			description = "잘못된 형식으로 요청했을 때",
			responseCode = "400",
			content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
		),
		@ApiResponse(
			description = "서버 에러",
			responseCode = "500",
			content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
		)
	})
	@GetMapping("/rooms/{roomId}")
	public DataResponseBody<GetRoomResponse> getRoom(@PathVariable Long roomId) {
		FindRoomRequest findRequest = new FindRoomRequest(roomId);
		FindRoomResponse findResult = roomQuery.findRoom(findRequest);
		GetRoomResponse result = mapper.findResultToDto(findResult);
		return DataResponseBody.success(result, "SUCCESS");
	}
}

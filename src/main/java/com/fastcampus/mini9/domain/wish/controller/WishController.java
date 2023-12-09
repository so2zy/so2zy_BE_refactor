package com.fastcampus.mini9.domain.wish.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.mini9.common.response.BaseResponseBody;
import com.fastcampus.mini9.common.response.DataResponseBody;
import com.fastcampus.mini9.config.security.token.UserPrincipal;
import com.fastcampus.mini9.domain.accommodation.controller.dto.AccommodationDtoMapper;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetAccommodationsResponse;
import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.accommodation.service.usecase.AccommodationQuery;
import com.fastcampus.mini9.domain.accommodation.service.util.AccommodationServiceMapper;
import com.fastcampus.mini9.domain.wish.entity.Wish;
import com.fastcampus.mini9.domain.wish.service.WishService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WishController {

	private final WishService wishService;
	private final AccommodationServiceMapper serviceMapper;
	private final AccommodationDtoMapper dtoMapper;

	@PostMapping("/accommodations/{accommodationId}/wish")
	public BaseResponseBody addWish(
		@PathVariable Long accommodationId,
		@AuthenticationPrincipal UserPrincipal principal
	) {
		wishService.addWish(accommodationId, principal);
		return BaseResponseBody.success("위시 등록 완료");
	}

	@DeleteMapping("/accommodations/{accommodationId}/wish")
	public BaseResponseBody deleteWish(
		@PathVariable Long accommodationId,
		@AuthenticationPrincipal UserPrincipal principal
	) {
		wishService.deleteWish(accommodationId, principal);
		return BaseResponseBody.success("위시 해제 완료");
	}

	@GetMapping("/wishes")
	public DataResponseBody<List<GetAccommodationsResponse.GetAccommodation>> findWishes(
		@AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<Wish> wishes = wishService.findWishes(userPrincipal);
		List<Accommodation> collect = wishes.stream().map(Wish::getAccommodation).collect(Collectors.toList());
		List<AccommodationQuery.SearchAccommodation> searchAccommodations = serviceMapper.entityListToResponseList(
			collect);
		List<GetAccommodationsResponse.GetAccommodation> getAccommodations = dtoMapper.searchResultToResponseDto(
			searchAccommodations);
		return DataResponseBody.success(getAccommodations);
	}
}

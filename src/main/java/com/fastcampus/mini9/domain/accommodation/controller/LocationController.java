package com.fastcampus.mini9.domain.accommodation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.mini9.common.response.DataResponseBody;
import com.fastcampus.mini9.common.response.ErrorResponseBody;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetDistrictsResponse;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetRegionsResponse;
import com.fastcampus.mini9.domain.accommodation.entity.location.District;
import com.fastcampus.mini9.domain.accommodation.entity.location.Region;
import com.fastcampus.mini9.domain.accommodation.repository.RegionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LocationController {
	private final RegionRepository regionRepository;

	@Operation(summary = "시/도 리스트")
	@ApiResponses(value = {
		@ApiResponse(
			description = "정상적으로 호출했을 때",
			responseCode = "200",
			useReturnTypeSchema = true
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
	@GetMapping("/locations/regions")
	public DataResponseBody<GetRegionsResponse> getRegions() {
		List<Region> regions = regionRepository.findAll();
		List<GetRegionsResponse.GetRegion> collect = regions.stream()
			.map(GetRegionsResponse.GetRegion::fromEntity)
			.collect(Collectors.toList());
		GetRegionsResponse result = new GetRegionsResponse(collect);
		return DataResponseBody.success(result, "SUCCESS");
	}

	@Operation(summary = "시/군/구 리스트")
	@ApiResponses(value = {
		@ApiResponse(
			description = "정상적으로 호출했을 때",
			responseCode = "200",
			useReturnTypeSchema = true
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
	@GetMapping("/locations/regions/{regionId}/districts")
	public DataResponseBody<GetDistrictsResponse> getDistricts(@PathVariable Long regionId) {
		Region region = regionRepository.findById(regionId).orElseThrow();
		List<District> districts = region.getDistricts();
		List<GetDistrictsResponse.GetDistrict> collect = districts.stream()
			.map(GetDistrictsResponse.GetDistrict::fromEntity)
			.collect(Collectors.toList());
		GetDistrictsResponse result = new GetDistrictsResponse(collect);
		return DataResponseBody.success(result, "SUCCESS");
	}
}

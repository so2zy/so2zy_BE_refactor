package com.fastcampus.mini9.domain.accommodation.controller.dto.response;

import java.util.List;

import com.fastcampus.mini9.domain.accommodation.entity.location.District;

public record GetDistrictsResponse(
	List<GetDistrict> districts
) {
	public record GetDistrict(
		Long id,
		String name
	) {
		public static GetDistrict fromEntity(District district) {
			return new GetDistrict(district.getId(), district.getName());
		}
	}
}

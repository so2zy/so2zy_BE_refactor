package com.fastcampus.mini9.domain.accommodation.controller.dto.response;

import java.util.List;

import com.fastcampus.mini9.domain.accommodation.entity.location.Region;

public record GetRegionsResponse(
	List<GetRegion> regions
) {
	public record GetRegion(
		Long id,
		String name
	) {
		public static GetRegion fromEntity(Region region) {
			return new GetRegion(region.getId(), region.getName());
		}
	}
}

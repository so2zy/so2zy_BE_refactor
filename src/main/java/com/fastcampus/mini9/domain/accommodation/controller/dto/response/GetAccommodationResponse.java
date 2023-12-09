package com.fastcampus.mini9.domain.accommodation.controller.dto.response;

import java.util.List;

public record GetAccommodationResponse(
	Long id,
	String name,
	String type,
	GetAccommodationDescription description,
	String region,
	String district,
	boolean isWish,
	Long totalWishCounts,
	List<String> accommodation_image
) {
	public record GetAccommodationDescription(
		String description,
		String address,
		String latitude,
		String longitude,
		String tel,
		boolean parking,
		boolean cooking,
		String others
	) {

	}
}

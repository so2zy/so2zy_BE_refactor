package com.fastcampus.mini9.domain.accommodation.controller.dto.response;

public record GetRoomResponse(
	Long id,
	String name,
	Integer price,
	Integer capacity,
	Integer capacity_max,
	GetRoomDescription description
) {
	public record GetRoomDescription(
		boolean airConditioner,
		boolean bathFacility,
		boolean bathtub,
		boolean cookware,
		boolean diningTable,
		boolean hairDryer,
		boolean homeTheater,
		boolean internet,
		boolean pc,
		boolean refrigerator,
		boolean sofa,
		boolean toiletries,
		boolean tv
	) {
	}
}

package com.fastcampus.mini9.domain.accommodation.service.usecase;

import java.time.LocalDate;
import java.util.List;

import com.fastcampus.mini9.domain.accommodation.entity.room.RoomDetails;

public interface RoomQuery {
	FindRoomsInAccommodationResponse findRoomsInAccommodation(FindRoomsInAccommodationRequest request);

	FindRoomResponse findRoom(FindRoomRequest request);

	record FindRoomsInAccommodationRequest(
		Long accommodationId,
		LocalDate startDate,
		LocalDate endDate,
		Long guestNum
	) {
	}

	record FindRoomsInAccommodationResponse(
		List<RoomResponse> roomResponses
	) {
	}

	record RoomResponse(
		Long id,
		String name,
		Integer price,
		Integer capacity,
		Integer capacity_max,
		Description description,
		Integer stock
	) {
	}

	record Description(
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
		public static Description fromEntity(RoomDetails roomDetails) {
			return new Description(
				roomDetails.isAirConditioner(),
				roomDetails.isBathFacility(),
				roomDetails.isBathtub(),
				roomDetails.isCookware(),
				roomDetails.isDiningTable(),
				roomDetails.isHairDryer(),
				roomDetails.isHomeTheater(),
				roomDetails.isInternet(),
				roomDetails.isPc(),
				roomDetails.isRefrigerator(),
				roomDetails.isSofa(),
				roomDetails.isToiletries(),
				roomDetails.isTv()
			);
		}
	}

	record FindRoomRequest(
		Long roomId
	) {
	}

	record FindRoomResponse(
		Long id,
		String name,
		Integer price,
		Integer capacity,
		Integer capacity_max,
		Description description,
		Integer stock
	) {
	}
}

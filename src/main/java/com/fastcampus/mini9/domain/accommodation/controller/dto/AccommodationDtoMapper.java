package com.fastcampus.mini9.domain.accommodation.controller.dto;

import static com.fastcampus.mini9.domain.accommodation.service.usecase.AccommodationQuery.*;
import static com.fastcampus.mini9.domain.accommodation.service.usecase.RoomQuery.*;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetAccommodationResponse;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetAccommodationsResponse;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetRoomResponse;
import com.fastcampus.mini9.domain.accommodation.controller.dto.response.GetRoomsResponse;

/**
 * 사용방법
 * @Mapping(source = "numberOfSeats", target = "seatCount")
 * CarDto carToCarDto(Car car);
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccommodationDtoMapper {
	// getAccommodations()
	@Mapping(target = "accommodations", source = "accommodationResponses")
	GetAccommodationsResponse searchResultToResponseDto(SearchAccommodationsResponse searchResult);

	List<GetAccommodationsResponse.GetAccommodation> searchResultToResponseDto(
		List<SearchAccommodation> accommodationResponses);

	GetAccommodationsResponse.GetAccommodation accommodationResponseToResDto(
		SearchAccommodation accommodationResponse);

	// getAccommodation()
	// TODO: WishCount 기능 구현 후 제거
	@Mapping(target = "totalWishCounts", constant = "10L")
	GetAccommodationResponse findResultToDto(FindAccommodationResponse findResult);

	// getRooms()
	@Mapping(target = "rooms", source = "roomResponses")
	GetRoomsResponse findResultToDto(FindRoomsInAccommodationResponse findResult);

	List<GetRoomsResponse.GetRoom> findResultToDto(List<RoomResponse> findResult);

	@Mapping(target = "stock_quantity", source = "stock")
	GetRoomsResponse.GetRoom findResultToDto(RoomResponse findResult);

	// getRoom()
	GetRoomResponse findResultToDto(FindRoomResponse findResult);
}


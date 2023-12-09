package com.fastcampus.mini9.domain.accommodation.service.util;

import static com.fastcampus.mini9.domain.accommodation.service.usecase.AccommodationQuery.*;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.accommodation.entity.accommodation.AccommodationDetails;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccommodationServiceMapper {
	// searchAccommodations()
	List<SearchAccommodation> entityListToResponseList(List<Accommodation> accommodationList);

	@Mapping(target = "region", source = "accommodation.location.region.name")
	@Mapping(target = "district", source = "accommodation.location.district.name")
	@Mapping(target = "thumbnail", source = "accommodation.thumbnail")
	@Mapping(target = "min_price", source = "accommodation.minPrice")
		// TODO: @Mapping(target = "isWish, source = "?")
	SearchAccommodation entityToResponse(Accommodation accommodation);

	// findAccommodation()
	@Mapping(target = "region", source = "accommodation.location.region.name")
	@Mapping(target = "district", source = "accommodation.location.district.name")
	@Mapping(target = "description", source = "accommodation.details")
	// TODO: @Mapping(target = "isWish", source = "?")
	// TODO: @Mapping(target = "totalWishCounts", source = "?")
	@Mapping(target = "accommodation_image", source = "accommodation.imagesAsString")
	FindAccommodationResponse entityToResponseDetail(Accommodation accommodation);

	FindAccommodationDescription entityToDescription(AccommodationDetails details);
}

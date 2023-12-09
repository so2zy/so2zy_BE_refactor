package com.fastcampus.mini9.domain.cart.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
// @Mapper(componentModel = "spring")
public interface CartDtoMapper {

	// @Mapping(target = "cart.paymentStatus", constant = "PENDING")
	// @Mapping(target = "roomId", ignore = true)
	// Cart toEntity(CreateCartRequest createCartRequest, Member member, Room room);
	// Cart toEntity(CreateCartRequest createCartRequest);
}

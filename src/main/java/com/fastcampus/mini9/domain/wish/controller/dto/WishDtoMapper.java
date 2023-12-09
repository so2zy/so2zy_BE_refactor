package com.fastcampus.mini9.domain.wish.controller.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.member.entity.Member;
import com.fastcampus.mini9.domain.wish.entity.Wish;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WishDtoMapper {
	WishDtoMapper INSTANCE = Mappers.getMapper(WishDtoMapper.class);

	Wish fromWishDto(Accommodation accommodation, Member member);

}

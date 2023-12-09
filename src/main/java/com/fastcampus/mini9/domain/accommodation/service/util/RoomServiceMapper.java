package com.fastcampus.mini9.domain.accommodation.service.util;

import static com.fastcampus.mini9.domain.accommodation.service.usecase.RoomQuery.*;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.fastcampus.mini9.domain.accommodation.entity.room.Room;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomServiceMapper {

	// findRoom()
	@Mapping(target = "capacity_max", source = "capacityMax")
	@Mapping(target = "stock", constant = "2")
	@Mapping(target = "description", source = "room.details")
	FindRoomResponse entityToFind(Room room);
}

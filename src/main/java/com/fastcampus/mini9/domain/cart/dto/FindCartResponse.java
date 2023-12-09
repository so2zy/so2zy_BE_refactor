package com.fastcampus.mini9.domain.cart.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fastcampus.mini9.domain.accommodation.vo.AccommodationType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FindCartResponse(

	@Schema(example = "조선 호텔 (숙소명)")
	String accommodationName,

	@Schema(example = "서울시 동대문구 (숙소 주소)")
	String address,

	List<FindCartRoomInfo> roomInfos) {

	public record FindCartRoomInfo(

		@Schema(example = "1")
		Long cartId,

		@Schema(example = "2")
		int quantity,

		@Schema(example = "스위트룸 (객실명)")
		String roomName,

		@Schema(example = "숙소 썸네일 Image URL")
		String accommodationThumbnailUrl,

		@Schema(example = "50000")
		int price,

		@Schema(example = "2023-11-11")
		LocalDate checkInDate,

		@Schema(example = "2023-11-15")
		LocalDate checkOutDate,

		@Schema(example = "15:00")
		LocalTime checkInTime,

		@Schema(example = "11:00")
		LocalTime checkOutTime,

		@Schema(example = "2")
		int capacity,

		@Schema(example = "4")
		int capacityMax,

		@Schema(example = "객실 타입")
		AccommodationType accommodationType) {

		public String getCheckInTime() {
			return checkInTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		}

		public String getCheckOutTime() {
			return checkOutTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		}
	}
}

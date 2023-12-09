package com.fastcampus.mini9.domain.reservation.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fastcampus.mini9.domain.accommodation.vo.AccommodationType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FindPaymentResponse(

	@Schema(example = "예약자 이름")
	String reservationUserName,

	@Schema(example = "예약자 이메일")
	String reservationUserEmail,

	@Schema(example = "이용자 이름")
	String guestName,

	@Schema(example = "이용자 이메일")
	String guestEmail,

	@Schema(example = "조선 호텔 (숙소명)")
	String accommodationName,

	@Schema(example = "숙소 타입")
	AccommodationType accommodationType,

	@Schema(example = "숙소 썸네일 URL")
	String accommodationThumbnailUrl,

	FindPaymentRoomInfo roomInfo) {

	public record FindPaymentRoomInfo(

		@Schema(example = "스위트룸 (객실명)")
		String roomName,

		@Schema(example = "50000")
		int price,

		@Schema(example = "2023-11-24 15:00")
		LocalDateTime checkIn,

		@Schema(example = "2023-11-26 11:00")
		LocalDateTime checkOut,

		@Schema(example = "2")
		int capacity,

		@Schema(example = "4")
		int capacityMax,

		Integer quantity
	) {
		public String getCheckIn() {
			return checkIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}

		public String getCheckOut() {
			return checkOut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}
	}
}

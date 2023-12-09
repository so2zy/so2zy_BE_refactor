package com.fastcampus.mini9.domain.payment.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fastcampus.mini9.domain.payment.entity.PaymentStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FindAllPaymentResponse(

	@Schema(example = "조선 호텔 (숙소명)")
	String accommodationName,

	@Schema(example = "결제 상태")
	PaymentStatus paymentStatus,
	
	FindAllPaymentResponse.RoomInfo roomInfo) {

	public record RoomInfo(

		Long paymentId,

		@Schema(example = "스위트룸 (객실명)")
		String roomName,

		@Schema(example = "2023-11-24 15:00")
		LocalDateTime checkIn,

		@Schema(example = "2023-11-26 11:00")
		LocalDateTime checkOut,

		@Schema(example = "2023-10-10")
		LocalDateTime payAt,
		String thumbnail
	) {
		public String getCheckIn() {
			return checkIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}

		public String getCheckOut() {
			return checkOut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}

		public String getPayAt() {
			return payAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
	}
}

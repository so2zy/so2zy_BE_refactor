package com.fastcampus.mini9.domain.payment.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fastcampus.mini9.domain.payment.entity.PaymentStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FindDetailPaymentResponse(

	@Schema(example = "20231124aBcdfEZs")
	String reservationNo,

	@Schema(example = "조선 호텔 (숙소명)")
	String accommodationName,

	@Schema(example = "스위트룸 (객실명)")
	String roomName,

	@Schema(example = "2023-11-24 15:00")
	LocalDateTime checkIn,

	@Schema(example = "2023-11-26 11:00")
	LocalDateTime checkOut,

	@Schema(example = "예약자 이름")
	String reservationUserName,

	@Schema(example = "예약자 이메일")
	String reservationUserEmail,

	@Schema(example = "이용자 이름")
	String guestName,

	@Schema(example = "이용자 이메일")
	String guestEmail,

	@Schema(example = "결제 금액")
	int price,

	@Schema(example = "결제 상태")
	PaymentStatus paymentStatus
) {
	public String getCheckIn() {
		return checkIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

	public String getCheckOut() {
		return checkOut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}
}

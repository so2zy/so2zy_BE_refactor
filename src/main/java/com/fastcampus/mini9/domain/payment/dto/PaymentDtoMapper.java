package com.fastcampus.mini9.domain.payment.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.fastcampus.mini9.domain.payment.entity.Payment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentDtoMapper {

	@Mapping(source = "payment.reservation.reservationNo", target = "reservationNo")
	@Mapping(source = "payment.room.accommodation.name", target = "accommodationName")
	@Mapping(source = "payment.room.name", target = "roomName")
	@Mapping(source = "payment.reservation.checkIn", target = "checkIn")
	@Mapping(source = "payment.reservation.checkOut", target = "checkOut")
	@Mapping(source = "payment.member.name", target = "reservationUserName")
	@Mapping(source = "payment.member.email", target = "reservationUserEmail")
	@Mapping(source = "payment.reservation.guestName", target = "guestName")
	@Mapping(source = "payment.reservation.guestEmail", target = "guestEmail")
	@Mapping(source = "payment.price", target = "price")
	@Mapping(source = "payment.status", target = "paymentStatus")
	FindDetailPaymentResponse paymentToDetailResponse(Payment payment);
}

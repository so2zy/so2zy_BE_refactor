package com.fastcampus.mini9.domain.cart.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateCartRequest(

	@NotNull
	@Min(1)
	Long roomId,

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Future
	LocalDate checkInDate,

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Future
	LocalDate checkOutDate) {
}

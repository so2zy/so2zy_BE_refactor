package com.fastcampus.mini9.domain.cart.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateOrderRequest(

	@NotEmpty(message = "객실을 선택해주세요.")
	List<Long> cartIds,

	@Schema(example = "박경탁 (게스트 이름)")
	@NotBlank
	String guestName,

	@Schema(example = "aaa@naver.com (게스트 이메일)")
	@NotBlank
	String guestEmail) {
}

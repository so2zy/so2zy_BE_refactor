package com.fastcampus.mini9.domain.payment.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.mini9.common.response.BaseResponseBody;
import com.fastcampus.mini9.common.response.DataResponseBody;
import com.fastcampus.mini9.config.security.token.UserPrincipal;
import com.fastcampus.mini9.domain.payment.dto.FindAllPaymentResponse;
import com.fastcampus.mini9.domain.payment.dto.FindDetailPaymentResponse;
import com.fastcampus.mini9.domain.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentService paymentService;

	@Operation(summary = "예약, 결제 전체 내역 조회")
	@GetMapping
	public DataResponseBody<List<FindAllPaymentResponse>> findAll(
		@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return DataResponseBody.success(paymentService.findAll(userPrincipal.id()));
	}

	@Operation(summary = "예약, 결제 상세 내역 조회")
	@GetMapping("/{paymentId}")
	public DataResponseBody<FindDetailPaymentResponse> findDetail(@PathVariable Long paymentId) {
		return DataResponseBody.success(paymentService.findDetail(paymentId));
	}

	@Operation(summary = "예약, 결제 취소")
	@DeleteMapping("/{paymentId}")
	public BaseResponseBody delete(@PathVariable Long paymentId) {
		paymentService.delete(paymentId);

		return DataResponseBody.success();
	}
}

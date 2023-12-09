package com.fastcampus.mini9.domain.cart.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.mini9.common.response.BaseResponseBody;
import com.fastcampus.mini9.common.response.DataResponseBody;
import com.fastcampus.mini9.config.security.token.UserPrincipal;
import com.fastcampus.mini9.domain.cart.dto.CartIdRequest;
import com.fastcampus.mini9.domain.cart.dto.CartIdsRequest;
import com.fastcampus.mini9.domain.cart.dto.CreateCartRequest;
import com.fastcampus.mini9.domain.cart.dto.CreateOrderRequest;
import com.fastcampus.mini9.domain.cart.dto.FindCartResponse;
import com.fastcampus.mini9.domain.cart.service.CartService;
import com.fastcampus.mini9.domain.reservation.dto.FindPaymentResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

	private final CartService cartService;

	@Operation(summary = "장바구니 조회")
	@GetMapping
	public DataResponseBody<List<FindCartResponse>> findAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return DataResponseBody.success(cartService.findCarts(userPrincipal.id()), "SUCCESS");
	}

	@Operation(summary = "장바구니 객실 담기")
	@PostMapping
	public BaseResponseBody createCart(@RequestBody CreateCartRequest createCartRequest,
		@AuthenticationPrincipal UserPrincipal userPrincipal) {
		cartService.addCart(createCartRequest, userPrincipal.id());

		return BaseResponseBody.success();
	}

	@Operation(summary = "장바구니 객실 삭제")
	@DeleteMapping
	public BaseResponseBody deleteCart(@RequestBody CartIdsRequest cartIdsRequest) {
		cartService.removeCart(cartIdsRequest);

		return BaseResponseBody.success();
	}

	@Operation(summary = "장바구니 객실 수량 증가")
	@PutMapping("/increase")
	public BaseResponseBody increaseCart(@RequestBody CartIdRequest cartIdRequest) {
		cartService.increaseCart(cartIdRequest);

		return BaseResponseBody.success();
	}

	@Operation(summary = "장바구니 객실 수량 감소")
	@PutMapping("/decrease")
	public BaseResponseBody decreaseCart(@RequestBody CartIdRequest cartIdRequest) {
		cartService.decreaseCart(cartIdRequest);

		return BaseResponseBody.success();
	}

	@Operation(summary = "예약하기 버튼")
	@PostMapping("/orders/reservations")
	public DataResponseBody<List<FindCartResponse>> findOrders(@RequestBody CartIdsRequest cartIdsRequest) {
		return DataResponseBody.success(cartService.findOrders(cartIdsRequest), "SUCCESS");
	}

	@Operation(summary = "결제하기 버튼")
	@PostMapping("/orders/payments")
	public BaseResponseBody createOrders(
		@RequestBody CreateOrderRequest createOrderRequest,
		@AuthenticationPrincipal UserPrincipal userPrincipal) {
		cartService.createOrder(createOrderRequest, userPrincipal.id());

		return DataResponseBody.success();
	}

	@Operation(summary = "결제하기 버튼 클릭 후 예약, 결제 내역 조회")
	@GetMapping("/orders/payments")
	public DataResponseBody<List<FindPaymentResponse>> findRecentOrders(
		@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam int count) {
		return DataResponseBody.success(cartService.findRecentOrders(userPrincipal.id(), count), "SUCCESS");
	}

	@Operation(summary = "바로 결제 버튼")
	@PostMapping("/orders/payments-eager")
	public BaseResponseBody createOrdersEager(@RequestBody CreateCartRequest createCartRequest,
		@AuthenticationPrincipal UserPrincipal userPrincipal) {
		Long cartId = cartService.addCart(createCartRequest, userPrincipal.id());

		return DataResponseBody.success(cartService.findOrders(new CartIdsRequest(List.of(cartId))), "SUCCESS");
	}
}

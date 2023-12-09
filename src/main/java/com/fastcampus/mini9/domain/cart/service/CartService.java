package com.fastcampus.mini9.domain.cart.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.accommodation.entity.room.Room;
import com.fastcampus.mini9.domain.accommodation.repository.RoomRepository;
import com.fastcampus.mini9.domain.cart.dto.CartIdRequest;
import com.fastcampus.mini9.domain.cart.dto.CartIdsRequest;
import com.fastcampus.mini9.domain.cart.dto.CreateCartRequest;
import com.fastcampus.mini9.domain.cart.dto.CreateOrderRequest;
import com.fastcampus.mini9.domain.cart.dto.FindCartResponse;
import com.fastcampus.mini9.domain.cart.entity.Cart;
import com.fastcampus.mini9.domain.cart.repository.CartRepository;
import com.fastcampus.mini9.domain.member.entity.Member;
import com.fastcampus.mini9.domain.member.repository.MemberRepository;
import com.fastcampus.mini9.domain.payment.entity.Payment;
import com.fastcampus.mini9.domain.payment.entity.PaymentStatus;
import com.fastcampus.mini9.domain.payment.repository.PaymentRepository;
import com.fastcampus.mini9.domain.reservation.dto.FindPaymentResponse;
import com.fastcampus.mini9.domain.reservation.entity.Reservation;
import com.fastcampus.mini9.domain.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

	private final CartRepository cartRepository;
	private final MemberRepository memberRepository;
	private final RoomRepository roomRepository;
	private final ReservationRepository reservationRepository;
	private final PaymentRepository paymentRepository;

	public List<FindCartResponse> findCarts(Long memberId) {
		Map<Long, List<Cart>> cartsByAccommodationId = cartRepository.findByMemberId(memberId).stream()
			.collect(Collectors.groupingBy(cart -> cart.getRoom().getAccommodation().getId()));

		return cartsByAccommodationId.values().stream()
			.map(this::mapToFindCartResponse)
			.collect(Collectors.toList());
	}

	private FindCartResponse mapToFindCartResponse(List<Cart> carts) {
		Accommodation accommodation = carts.get(0).getRoom().getAccommodation();

		return new FindCartResponse(
			accommodation.getName(), // 숙소명
			accommodation.getDetails().getAddress(), // 숙소 주소
			carts.stream()
				.map(cart -> {
					Room room = cart.getRoom();

					return new FindCartResponse.FindCartRoomInfo(
						cart.getId(), // 장바구니 ID
						cart.getQuantity(), // 수량
						room.getName(), // 객실명
						room.getAccommodation().getThumbnail(), // 숙소 썸네일 url
						room.getPrice(), // 객실 가격
						cart.getCheckInDate(), // 체크인 날짜
						cart.getCheckOutDate(), // 체크아웃 날짜
						room.getAccommodation().getCheckIn(), // 체크인 시간
						room.getAccommodation().getCheckOut(), // 체크아웃 시간
						room.getCapacity(), // 기준 인원
						room.getCapacityMax(), // 최대 인원
						accommodation.getType() // 객실 타입
					);
				}).collect(Collectors.toList())
		);
	}

	@Transactional
	public Long addCart(CreateCartRequest dto, Long id) {
		Member member = memberRepository.findById(id).orElseThrow();
		Room room = roomRepository.findById(dto.roomId()).orElseThrow();

		// TODO: 체크인, 체크아웃 날짜 검증

		Optional<Cart> optionalCart = cartRepository.findByCheckInDateAndCheckOutDateAndMemberIdAndRoomId(
			dto.checkInDate(), dto.checkOutDate(), member.getId(), room.getId());

		if (optionalCart.isEmpty()) {
			Cart cart = cartRepository.save(Cart.builder()
				.checkInDate(dto.checkInDate())
				.checkOutDate(dto.checkOutDate())
				.member(member)
				.room(room)
				.build());

			return cart.getId();
		}
		Cart cart = optionalCart.get();
		cart.increaseQuantity();

		return cart.getId();
	}

	@Transactional
	public void removeCart(CartIdsRequest dto) {
		dto.cartIds().forEach(cartId -> {
			Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new NoSuchElementException("장바구니에 해당하는 객실이 존재하지 않습니다."));

			cartRepository.delete(cart);
		});
	}

	public List<FindCartResponse> findOrders(CartIdsRequest dto) {
		Map<Long, List<Cart>> cartsByAccommodationId = dto.cartIds().stream()
			.map(cartId -> cartRepository.findById(cartId)
				.orElseThrow(() -> new NoSuchElementException("장바구니에 해당하는 객실이 존재하지 않습니다.")))
			.collect(Collectors.groupingBy(cart -> cart.getRoom().getAccommodation().getId()));

		return cartsByAccommodationId.values().stream()
			.map(this::mapToFindCartResponse)
			.collect(Collectors.toList());
	}

	@Transactional
	public void increaseCart(CartIdRequest dto) {
		Cart cart = cartRepository.findById(dto.cartId())
			.orElseThrow(() -> new NoSuchElementException("장바구니에 해당하는 객실이 존재하지 않습니다."));

		// TODO: 재고 검증(수량이 재고보다 클 수 없다.)

		cart.increaseQuantity();
	}

	@Transactional
	public void decreaseCart(CartIdRequest dto) {
		Cart cart = cartRepository.findById(dto.cartId())
			.orElseThrow(() -> new NoSuchElementException("장바구니에 해당하는 객실이 존재하지 않습니다."));

		if (cart.getQuantity() <= 1) {
			throw new IllegalArgumentException("수량이 1 보다 작을 수 없습니다.");
		}
		cart.decreaseQuantity();
	}

	@Transactional
	public void createOrder(CreateOrderRequest dto, Long memberId) {

		// TODO: 숙박일 검증

		Member member = memberRepository.findById(memberId).orElseThrow();
		List<Cart> carts = cartRepository.findAllById(dto.cartIds());

		if (carts.size() != dto.cartIds().size()) {
			throw new IllegalArgumentException();
		}

		for (Cart cart : carts) {

			// TODO: 재고 검증
			// TODO: 재고 감소

			// 결제 생성
			Payment payment = Payment.builder()
				.payAt(LocalDateTime.now())
				.price(cart.getRoom().getPrice())
				.quantity(cart.getQuantity())
				.status(PaymentStatus.COMPLETED)
				.member(member)
				.room(cart.getRoom())
				.build();

			// 예약 생성
			Reservation reservation = Reservation.builder()
				.member(member)
				.checkIn(cart.getCheckInDate().atTime(cart.getRoom().getAccommodation().getCheckIn()))
				.checkOut(cart.getCheckOutDate().atTime(cart.getRoom().getAccommodation().getCheckOut()))
				.guestName(dto.guestName())
				.guestEmail(dto.guestEmail())
				.reservationNo(UUID.randomUUID().toString())
				.build();
			reservation.setPayment(payment);

			reservationRepository.save(reservation);

			// TODO: 무조건 삭제 시키는게 아니라 수량을 감소 시킬 수도 있다.
			cartRepository.delete(cart);
		}
	}

	public List<FindPaymentResponse> findRecentOrders(Long memberId, int recentOrderCnt) {
		List<Payment> payments = paymentRepository.findByMemberIdOrderByPayAtDesc(memberId,
			PageRequest.of(0, recentOrderCnt));
		List<FindPaymentResponse> findPaymentResponses = new ArrayList<>();

		for (Payment payment : payments) {
			Reservation reservation = payment.getReservation();
			Member member = reservation.getMember();
			Room room = payment.getRoom();
			Accommodation accommodation = room.getAccommodation();

			findPaymentResponses.add(
				new FindPaymentResponse(member.getName(), member.getEmail(), reservation.getGuestName(),
					reservation.getGuestEmail(), accommodation.getName(), accommodation.getType(),
					accommodation.getThumbnail(),
					new FindPaymentResponse.FindPaymentRoomInfo(room.getName(), payment.getPrice(),
						reservation.getCheckIn(), reservation.getCheckOut(), room.getCapacity(),
						room.getCapacityMax(), payment.getQuantity())));
		}
		return findPaymentResponses;
	}
}

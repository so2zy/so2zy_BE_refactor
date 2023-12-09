package com.fastcampus.mini9.domain.cart.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.mini9.domain.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	List<Cart> findByMemberId(Long memberId);

	Optional<Cart> findByCheckInDateAndCheckOutDateAndMemberIdAndRoomId(LocalDate checkInDate, LocalDate checkOutDate,
		Long memberId, Long roomId);
}

package com.fastcampus.mini9.domain.payment.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.mini9.domain.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByMemberId(Long memberId);

	List<Payment> findByMemberIdOrderByPayAtDesc(Long memberId, Pageable pageable);
}

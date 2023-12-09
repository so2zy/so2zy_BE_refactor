package com.fastcampus.mini9.domain.payment.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fastcampus.mini9.domain.accommodation.entity.room.Room;
import com.fastcampus.mini9.domain.payment.dto.FindAllPaymentResponse;
import com.fastcampus.mini9.domain.payment.dto.FindDetailPaymentResponse;
import com.fastcampus.mini9.domain.payment.dto.PaymentDtoMapper;
import com.fastcampus.mini9.domain.payment.entity.Payment;
import com.fastcampus.mini9.domain.payment.repository.PaymentRepository;
import com.fastcampus.mini9.domain.reservation.entity.Reservation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

	private final PaymentDtoMapper paymentDtoMapper;

	public List<FindAllPaymentResponse> findAll(Long userId) {
		List<Payment> payments = paymentRepository.findAll();

		if (payments.isEmpty()) {
			return List.of();
		}
		return payments.stream()
			.filter(payment -> payment.getMember().getId().equals(userId))
			.map(payment -> {
			Room room = payment.getRoom();
			Reservation reservation = payment.getReservation();

			if (room == null || reservation == null) {
				throw new NoSuchElementException();
			}
			return new FindAllPaymentResponse(room.getAccommodation().getName(), payment.getStatus(),
				new FindAllPaymentResponse.RoomInfo(
					payment.getId(), room.getName(), reservation.getCheckIn(), reservation.getCheckOut(),
					payment.getPayAt(), room.getAccommodation().getThumbnail()
				));
		}).collect(Collectors.toList());
	}

	public FindDetailPaymentResponse findDetail(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new NoSuchElementException("해당 결제내역이 존재하지 않습니다."));

		return paymentDtoMapper.paymentToDetailResponse(payment);
	}

	@Transactional
	public void delete(Long paymentId) {
		paymentRepository.findById(paymentId).orElseThrow().cancel();
	}
}

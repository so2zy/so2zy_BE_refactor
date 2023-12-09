package com.fastcampus.mini9.domain.reservation.entity;

import java.time.LocalDateTime;

import com.fastcampus.mini9.domain.member.entity.Member;
import com.fastcampus.mini9.domain.payment.entity.Payment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	private LocalDateTime checkIn;

	private LocalDateTime checkOut;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "reservation", cascade = CascadeType.ALL)
	private Payment payment;

	private String guestName;

	private String guestEmail;

	private String reservationNo;

	public void setPayment(Payment payment) {
		this.payment = payment;
		payment.setReservation(this);
	}
}

package com.fastcampus.mini9.domain.payment.entity;

public enum PaymentStatus {
	CANCELED("결제취소"), PENDING("결제대기"), COMPLETED("결제완료");

	private final String label;

	PaymentStatus(String label) {
		this.label = label;
	}
}

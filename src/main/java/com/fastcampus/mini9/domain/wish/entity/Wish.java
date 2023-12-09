package com.fastcampus.mini9.domain.wish.entity;

import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Wish {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "accommodation_id")
	private Accommodation accommodation;
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public Wish(@NonNull Accommodation accommodation, @NonNull Member member) {
		this.accommodation = accommodation;
		this.member = member;
		member.getWishList().add(this);
	}
}

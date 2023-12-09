package com.fastcampus.mini9.domain.wish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;
import com.fastcampus.mini9.domain.member.entity.Member;
import com.fastcampus.mini9.domain.wish.entity.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {

	Boolean existsByAccommodationAndMember(Accommodation accommodation, Member member);

	void deleteByAccommodationAndMember(Accommodation accommodation, Member member);

	List<Wish> findAllByMember(Member member);
}

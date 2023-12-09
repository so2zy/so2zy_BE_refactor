package com.fastcampus.mini9.domain.accommodation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.mini9.domain.accommodation.entity.accommodation.Accommodation;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, AccommodationQueryDslRepository {
}

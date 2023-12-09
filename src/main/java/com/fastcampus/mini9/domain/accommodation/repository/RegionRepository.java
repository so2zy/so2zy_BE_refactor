package com.fastcampus.mini9.domain.accommodation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.mini9.domain.accommodation.entity.location.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
}

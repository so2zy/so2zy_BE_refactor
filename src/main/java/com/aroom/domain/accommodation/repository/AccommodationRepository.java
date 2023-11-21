package com.aroom.domain.accommodation.repository;

import com.aroom.domain.accommodation.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

}

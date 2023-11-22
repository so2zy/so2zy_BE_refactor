package com.aroom.domain.accommodation.repository;

import com.aroom.domain.accommodation.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> , AccommodationRepositoryCustom{

}

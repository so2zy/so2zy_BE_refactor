package com.aroom.domain.accommodation.repository;

import com.aroom.domain.accommodation.model.Accommodation;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> , AccommodationRepositoryCustom{

    @EntityGraph(attributePaths = "roomList")
    Optional<Accommodation> findById(Long accommodationId);
}

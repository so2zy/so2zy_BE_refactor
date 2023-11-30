package com.aroom.domain.favorite.repository;

import com.aroom.domain.favorite.model.Favorite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByMemberIdAndAccommodationId(Long memberId, Long accommodationId);

    boolean existsByMemberIdAndAccommodationId(Long memberId, Long accommodationId);
}

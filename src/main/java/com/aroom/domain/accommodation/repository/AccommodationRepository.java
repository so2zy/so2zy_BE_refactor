package com.aroom.domain.accommodation.repository;

import com.aroom.domain.accommodation.model.Accommodation;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long>,
    AccommodationRepositoryCustom {

    @EntityGraph(attributePaths = "roomList")
    Optional<Accommodation> findById(Long accommodationId);

    @Query("select ac from Accommodation ac join Favorite fav on ac.id = fav.accommodation.id where fav.member.id = :memberId and ac.id = :accommodationId")
    Optional<Accommodation> findByAccommodationIdAndMemberId(@Param("memberId") Long memberId,
        @Param("accommodationId") Long accommodationId);

    @Query("SELECT ac FROM Accommodation ac " +
        "JOIN FETCH ac.roomList r " +
        "WHERE ac.id = :accommodationId AND EXISTS (" +
        "    SELECT rp FROM RoomProduct rp " +
        "    WHERE rp.room.id = r.id AND rp.startDate = :startDate and (rp.startDate between :startDate and :endDate))")
    Optional<Accommodation> findByAccommodationIdAndStartDate(@Param("accommodationId") Long accommodationId,  @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}




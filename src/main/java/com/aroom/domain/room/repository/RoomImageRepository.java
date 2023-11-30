package com.aroom.domain.room.repository;

import com.aroom.domain.room.model.RoomImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
    Optional<RoomImage> findByRoomId(Long roomId);
}

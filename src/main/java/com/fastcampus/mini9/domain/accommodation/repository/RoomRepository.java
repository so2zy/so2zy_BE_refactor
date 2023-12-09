package com.fastcampus.mini9.domain.accommodation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastcampus.mini9.domain.accommodation.entity.room.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}

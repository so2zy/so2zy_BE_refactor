package com.aroom.domain.room.service;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import com.aroom.domain.room.dto.response.RoomResponseDTO;
import com.aroom.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final AccommodationRepository accommodationRepository;

    public RoomResponseDTO getRoom(Long accommodation_id) {
        Accommodation accommodation = accommodationRepository.findById(accommodation_id).get();
        return new RoomResponseDTO(accommodation);
    }
}

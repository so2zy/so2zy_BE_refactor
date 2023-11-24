package com.aroom.domain.accommodation.service;

import com.aroom.domain.accommodation.dto.response.AccommodationResponseDTO;
import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public AccommodationResponseDTO getRoom(Long accommodation_id) {
        Accommodation accommodation = accommodationRepository.findById(accommodation_id).orElseThrow(
            AccommodationNotFoundException::new);
        return new AccommodationResponseDTO(accommodation);
    }
}

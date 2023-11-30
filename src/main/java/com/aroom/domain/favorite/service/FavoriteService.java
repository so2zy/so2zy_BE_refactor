package com.aroom.domain.favorite.service;

import com.aroom.domain.accommodation.exception.AccommodationNotFoundException;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import com.aroom.domain.favorite.exception.AlreadyFavoriteException;
import com.aroom.domain.favorite.exception.FavoriteNotFoundException;
import com.aroom.domain.favorite.model.Favorite;
import com.aroom.domain.favorite.repository.FavoriteRepository;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.service.MemberQueryService;
import com.aroom.domain.member.service.MemberQueryService.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FavoriteService {

    private final MemberQueryService memberQueryService;
    private final FavoriteRepository favoriteRepository;
    private final AccommodationRepository accommodationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        MemberQueryService memberQueryService, AccommodationRepository accommodationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberQueryService = memberQueryService;
        this.accommodationRepository = accommodationRepository;
    }

    public void createFavorite(FavoriteCreateRequest request) {
        MemberResponse memberResponse = memberQueryService.getByMemberId(request.memberId());

        Accommodation targetAccommodation = accommodationRepository.findById(
            request.accommodationId()).orElseThrow(AccommodationNotFoundException::new);

        if (favoriteRepository.existsByMemberIdAndAccommodationId(request.memberId(),
            request.accommodationId())) {
            throw new AlreadyFavoriteException(
                "favorite already exist! / memberId : %d, accommodationId : %d".formatted(
                    request.memberId(), request.accommodationId()));
        }

        favoriteRepository.save(Favorite.builder()
            .accommodation(targetAccommodation)
            .member(Member.builder().id(memberResponse.memberId()).build())
            .build());
    }

    public void deleteFavorite(FavoriteCreateRequest request) {
        Favorite favorite = favoriteRepository.findByMemberIdAndAccommodationId(request.memberId(),
                request.accommodationId())
            .orElseThrow(() ->
                new FavoriteNotFoundException(
                    "favorite not found! / memberId : %d, accommodationId : %d"
                        .formatted(request.memberId(), request.accommodationId())));

        favoriteRepository.delete(favorite);
    }
}

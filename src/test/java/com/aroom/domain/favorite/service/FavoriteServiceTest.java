package com.aroom.domain.favorite.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import com.aroom.domain.favorite.model.Favorite;
import com.aroom.domain.favorite.repository.FavoriteRepository;
import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.service.MemberQueryService;
import com.aroom.domain.member.service.MemberQueryService.MemberResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private MemberQueryService memberQueryService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @DisplayName("createFavorite()는")
    @Nested
    class Context_createFavorite {

        @DisplayName("성공한다")
        @Test
        void _willSuccess() {

            // given
            FavoriteCreateRequest request = new FavoriteCreateRequest(1L, 1L);
            given(memberQueryService.getByMemberId(request.memberId()))
                .willReturn(new MemberResponse(1L, "test@email.com", "name"));

            given(accommodationRepository.findById(request.accommodationId()))
                .willReturn(Optional.of(Accommodation.builder().build()));

            // when
            favoriteService.createFavorite(request);

            // then
            verify(memberQueryService, times(1)).getByMemberId(request.memberId());
            verify(accommodationRepository, times(1)).findById(request.accommodationId());
            verify(favoriteRepository, times(1)).save(any(Favorite.class));
        }

        @DisplayName("회원을 찾지 못하면 MemberNotFoundException을 던진다.")
        @Test
        void cannotFindMember_wemberNotFoundException_willThrowns() {

            // given
            FavoriteCreateRequest request = new FavoriteCreateRequest(1L, 1L);
            given(memberQueryService.getByMemberId(request.memberId()))
                .willThrow(MemberNotFoundException.class);

            // when then
            assertThatThrownBy(() -> favoriteService.createFavorite(request))
                .isInstanceOf(MemberNotFoundException.class);
            verify(memberQueryService, times(1)).getByMemberId(request.memberId());
            verify(accommodationRepository, times(0)).findById(request.accommodationId());
            verify(favoriteRepository, times(0)).save(any(Favorite.class));
        }
    }
}

package com.aroom.domain.accommodation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.address.model.Address;
import com.aroom.domain.address.repository.AddressRepository;
import com.aroom.domain.favorite.model.Favorite;
import com.aroom.domain.favorite.repository.FavoriteRepository;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.repository.RoomRepository;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.domain.roomProduct.repository.RoomProductRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccommodationRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RoomProductRepository roomProductRepository;

    private Member member;
    private Address address;
    private Accommodation accommodation;
    private Room room;
    private Favorite favorite;
    private RoomProduct roomProduct;


    @BeforeEach
    private void init() {
        member = Member.builder().id(1L).email("yang980329@naver.com").name("양유림")
            .password("123!!!").build();
        memberRepository.save(member);

        address = Address.builder().id(1L).areaCode(1).sigunguCode(1).areaName("경기도")
            .sigunguName("고양시").build();
        addressRepository.save(address);

        accommodation = Accommodation.builder().id(1L)
            .addressEntity(address)
            .name("롯데호텔").latitude(
                (float) 150.54).longitude((float) 100.5)
            .phoneNumber("02-771-1000").address("경기도 고양시 일산동구").build();
        accommodationRepository.save(accommodation);

        room = Room.builder().id(1L).type("DELUXE").price(350000).capacity(2).maxCapacity(4)
            .checkIn(
                LocalTime.of(15, 0)).checkOut(LocalTime.of(11, 0)).accommodation(accommodation)
            .build();
        roomRepository.save(room);

        favorite = Favorite.builder().member(member).accommodation(accommodation).build();
        favoriteRepository.save(favorite);

        roomProduct = RoomProduct.builder().id(1L).room(room).startDate(LocalDate.of(2023, 11, 27))
            .stock(4)
            .build();
        roomProductRepository.save(roomProduct);
    }

    @Test
    @DisplayName("지정된 숙소가 조회 됐습니다.")
    void findAccommodationById() {
        // when
        Optional<Accommodation> result = accommodationRepository.findById(accommodation.getId());

        // then
        assertTrue(result.isPresent());
        assertThat(result.get().getName()).isEqualTo("롯데호텔");
        assertThat(result.get().getLatitude()).isEqualTo((float) 150.54);
        assertThat(result.get().getLongitude()).isEqualTo((float) 100.5);
        assertThat(result.get().getPhoneNumber()).isEqualTo("02-771-1000");
    }

    @Test
    @DisplayName("memberId와 AccommodationId를 토대로 Accommodation이 조회 됐습니다.")
    void findByAccommodationIdAndMemberId() {
        // given
        Long memberId = 1L;
        Long accommodationId = 1L;

        // when
        Optional<Accommodation> result = accommodationRepository.findByAccommodationIdAndMemberId(
            memberId, accommodationId);

        // then
        assertThat(result.get().getId()).isEqualTo(accommodationId);
        assertThat(result.get().getAddress()).isEqualTo("경기도 고양시 일산동구");
    }

    @Test
    @DisplayName("accommodationId, startDate, endDate 토대로 Accommodation이 조회 됐습니다.")
    void findByAccommodationIdAndStartDate() {
        // given
        LocalDate startDate = LocalDate.of(2023, 11, 27);
        LocalDate endDate = LocalDate.of(2023, 11, 28);
        Long accommodationId = 1L;

        // when
        Optional<Accommodation> result = accommodationRepository.findByAccommodationIdAndStartDate(
            accommodationId, startDate, endDate);

        // then
        assertTrue(result.isPresent());
        assertThat(result.get().getId()).isEqualTo(accommodationId);
        assertThat(result.get().getName()).isEqualTo("롯데호텔");
    }
}

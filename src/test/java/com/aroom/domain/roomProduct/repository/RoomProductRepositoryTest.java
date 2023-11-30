package com.aroom.domain.roomProduct.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.accommodation.repository.AccommodationRepository;
import com.aroom.domain.address.model.Address;
import com.aroom.domain.address.repository.AddressRepository;
import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.repository.RoomRepository;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import com.aroom.domain.roomProduct.model.RoomProduct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoomProductRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomProductRepository roomProductRepository;

    @Autowired
    private RoomCartRepository roomCartRepository;

    @Autowired
    private CartRepository cartRepository;

    private Member member;
    private Cart cart;
    private Address address;
    private Accommodation accommodation;
    private Room room;
    private RoomProduct roomProduct;
    private RoomCart roomCart;

    @BeforeEach
    private void init() {
        member = Member.builder().id(1L).email("yang980329@naver.com").name("양유림")
            .password("123!!!").build();
        memberRepository.save(member);

        cart = Cart.builder().id(1L).member(member).build();
        cartRepository.save(cart);

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

        roomProduct = RoomProduct.builder().id(1L).room(room).startDate(LocalDate.of(2023, 11, 27))
            .stock(4)
            .build();
        roomProductRepository.save(roomProduct);

        roomCart = RoomCart.builder().id(1L).roomProduct(roomProduct).cart(cart).personnel(3).build();
        roomCartRepository.save(roomCart);
    }

    @Test
    @DisplayName("roomId, startDate를 토대로 stock >0인 RoomProduct가 조회됐습니다.")
    void findByRoomIdAndStartDate() {
        // given
        Long roomId = 1L;
        LocalDate startDate = LocalDate.of(2023, 11, 27);

        // when
        Optional<RoomProduct> result = roomProductRepository.findByRoomIdAndStartDate(roomId,startDate);

        // then
        assertThat(result.get().getRoom().getId()).isEqualTo(roomId);
        assertThat(result.get().getStartDate()).isEqualTo(startDate);
    }

    @Test
    @DisplayName("roomId, startDate, endDate를 토대로 stock >0 인 RoomProduct가 조회됐습니다.")
    void findByRoomIdAndStartDateAndEndDate() {
        // given
        Long roomId = 1L;
        LocalDate startDate = LocalDate.of(2023, 11, 27);
        LocalDate endDate = LocalDate.of(2023, 11, 28);


        // when
        List<RoomProduct> result = roomProductRepository.findByRoomIdAndStartDateAndEndDate(roomId,startDate,endDate);

        // then
        assertThat(result.get(0).getRoom().getId()).isEqualTo(roomId);
        assertThat(result.get(0).getStartDate()).isEqualTo(startDate);
    }
}


package com.aroom.domain.roomCart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import com.aroom.domain.room.repository.RoomRepository;
import com.aroom.domain.roomCart.dto.request.RoomCartRequest;
import com.aroom.domain.roomCart.dto.response.FindCartResponse;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.domain.roomProduct.repository.RoomProductRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
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
public class RoomCartServiceTest {

    @InjectMocks
    private RoomCartService roomCartService;

    @Mock
    private RoomCartRepository roomCartRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private RoomProductRepository roomProductRepository;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("postRoomCart()은")
    class Context_postRoomCart {

        @Test
        @DisplayName("1박투숙도 장바구니에 객실을 등록할 수 있다.")
        void CartSave_willSuccess() {
            // given
            long memberId = 1L;
            long roomId = 1L;
            long roomProductId = 1L;
            RoomCartRequest roomCartRequest = RoomCartRequest.builder()
                .startDate(LocalDate.of(2023, 11, 27)).endDate(LocalDate.of(2023, 11, 28)).personnel(3).build();

            Member member = Member.builder().id(1L).email("yang980329@naver.com").name("양유림")
                .password("123!!!").build();
            Cart cart = Cart.builder().id(1L).member(member).roomCartList(new ArrayList<>())
                .build();
            given(cartRepository.findByMemberId(any(Long.TYPE))).willReturn(Optional.of(cart));

            Room room = Room.builder().id(1L).type("DELUXE").price(350000).capacity(2)
                .maxCapacity(4)
                .checkIn(
                    LocalTime.of(15, 0)).checkOut(LocalTime.of(11, 0))
                .build();
            RoomProduct roomProduct = RoomProduct.builder().id(roomProductId).room(room)
                .startDate(roomCartRequest.getStartDate()).stock(4).build();
            given(roomProductRepository.findByRoomIdAndStartDate(any(Long.TYPE),eq(roomCartRequest.getStartDate()))).willReturn(Optional.of(roomProduct));


            RoomCart roomCart = RoomCart.builder().id(1L).roomProduct(roomProduct).cart(cart)
                .build();
            List<RoomCart> roomCartList = List.of(roomCart);
            cart.postRoomCarts(roomCart);
            given(roomCartRepository.findByRoomProductId(any(Long.TYPE))).willReturn(roomCartList);
            given(roomCartRepository.save(any(RoomCart.class))).willReturn(roomCart);

            // when
            RoomCartResponse roomCartResponse = roomCartService.postRoomCart(memberId, roomId,
                roomCartRequest);

            // then
            assertThat(roomCartResponse.getRoomCartList().get(0).getRoom_id()).isEqualTo(roomId);
        }

        @Test
        @DisplayName("장기투숙도 장바구니에 객실을 등록할 수 있다.")
        void CartLongSave_willSuccess() {
            // given
            long memberId = 1L;
            long roomId = 1L;
            RoomCartRequest roomCartRequest = RoomCartRequest.builder()
                .startDate(LocalDate.of(2023, 11, 27)).endDate(LocalDate.of(2023, 11, 30)).personnel(3).build();

            Member member = Member.builder().id(1L).email("yang980329@naver.com").name("양유림")
                .password("123!!!").build();
            Cart cart = Cart.builder().id(1L).member(member).roomCartList(new ArrayList<>())
                .build();
            given(cartRepository.findByMemberId(any(Long.TYPE))).willReturn(Optional.of(cart));

            Room room = Room.builder().id(1L).type("DELUXE").price(350000).capacity(2)
                .maxCapacity(4)
                .checkIn(
                    LocalTime.of(15, 0)).checkOut(LocalTime.of(11, 0))
                .build();
            RoomProduct roomProduct1 = RoomProduct.builder().id(1L).room(room)
                .startDate(roomCartRequest.getStartDate()).stock(4).build();
            RoomProduct roomProduct2 = RoomProduct.builder().id(2L).room(room)
                .startDate(roomCartRequest.getStartDate()).stock(2).build();
            RoomProduct roomProduct3 = RoomProduct.builder().id(3L).room(room)
                .startDate(roomCartRequest.getStartDate()).stock(3).build();

            List<RoomProduct> roomProductList = new ArrayList<>();
            roomProductList.add(roomProduct1);
            roomProductList.add(roomProduct2);
            roomProductList.add(roomProduct3);
            given(roomProductRepository.findByRoomIdAndStartDateAndEndDate(any(Long.TYPE),
                eq(roomCartRequest.getStartDate()), eq(roomCartRequest.getEndDate().minusDays(1)))).willReturn(
                roomProductList);

            RoomCart roomCart1 = RoomCart.builder().id(1L).roomProduct(roomProduct1).cart(cart)
                .build();
            RoomCart roomCart2 = RoomCart.builder().id(2L).roomProduct(roomProduct2).cart(cart)
                .build();
            RoomCart roomCart3 = RoomCart.builder().id(3L).roomProduct(roomProduct3).cart(cart)
                .build();
            roomCartRepository.save(roomCart1);
            roomCartRepository.save(roomCart2);
            roomCartRepository.save(roomCart3);
            List<RoomCart> roomCartMinList = new ArrayList<>();
            roomCartMinList.add(roomCart2);
            cart.postRoomCarts(roomCart1);
            cart.postRoomCarts(roomCart2);
            cart.postRoomCarts(roomCart3);
            given(roomCartRepository.findByRoomProductId(roomProduct2.getId())).willReturn(roomCartMinList);
            given(roomCartRepository.save(any(RoomCart.class))).willReturn(roomCart1);

            // when
            RoomCartResponse roomCartResponse = roomCartService.postRoomCart(memberId, roomId,
                roomCartRequest);

            // then
            assertThat(roomCartResponse.getRoomCartList().get(0).getRoom_id()).isEqualTo(roomId);
        }


        @Test
        @DisplayName("객실이 이미 장바구니에 많이 담겨있다면, 장바구니에 더 담을 수 없다.")
        void outOfStockException_willFail() {
            // given
            long memberId = 1L;
            long roomId = 1L;
            long roomProductId = 1L;
            RoomCartRequest roomCartRequest = RoomCartRequest.builder()
                .startDate(LocalDate.of(2023, 11, 27)).endDate(LocalDate.of(2023, 11, 28)).personnel(3).build();

            Member member = Member.builder().id(1L).email("yang980329@naver.com").name("양유림")
                .password("123!!!").build();
            Cart cart = Cart.builder().id(1L).member(member).roomCartList(new ArrayList<>())
                .build();
            given(cartRepository.findByMemberId(any(Long.TYPE))).willReturn(Optional.of(cart));

            Room room = Room.builder().id(1L).type("DELUXE").price(350000).capacity(2)
                .maxCapacity(4)
                .checkIn(
                    LocalTime.of(15, 0)).checkOut(LocalTime.of(11, 0))
                .build();
            RoomProduct roomProduct = RoomProduct.builder().id(roomProductId).room(room)
                .startDate(roomCartRequest.getStartDate()).stock(1).build();

            given(roomProductRepository.findByRoomIdAndStartDate(any(Long.TYPE),
                eq(roomCartRequest.getStartDate()))).willReturn(
                Optional.of(roomProduct));

            RoomCart roomCart = RoomCart.builder().id(1L).roomProduct(roomProduct).cart(cart)
                .build();
            List<RoomCart> roomCartList = List.of(roomCart);
            cart.postRoomCarts(roomCart);
            given(roomCartRepository.findByRoomProductId(roomProductId)).willReturn(roomCartList);

            // when
            Throwable exception = assertThrows(OutOfStockException.class, () -> {
                roomCartService.postRoomCart(memberId, roomId,roomCartRequest);
            });

            // then
            assertEquals("상품의 재고 부족으로 장바구니 담기가 불가합니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("장바구니 조회는 ")
    class findRoomCart {

        @Test
        @DisplayName("등록된 장바구니가 있다면 조회할 수 있다.")
        void has_roomProduct_can_find_success() {
            // given
            Member tester = Member.builder()
                .name("tester")
                .email("tester@naver.com")
                .password("test123@")
                .build();

            Accommodation accommodation = Accommodation.builder().build();
            Room room = Room.builder()
                .accommodation(accommodation)
                .id(1L)
                .type("패밀리")
                .checkIn(LocalTime.now())
                .checkOut(LocalTime.now())
                .capacity(2)
                .maxCapacity(4)
                .price(100000)
                .roomImageList(List.of(RoomImage.builder().url("asdasdasd").build()))
                .build();

            RoomProduct roomProduct = RoomProduct.builder()
                .room(room)
                .startDate(LocalDate.now())
                .build();

            List<RoomCart> roomCartList = new ArrayList<>();
            roomCartList.add(RoomCart.builder()
                .roomProduct(roomProduct)
                .build());

            Cart cart = Cart.builder()
                .member(tester)
                .roomCartList(roomCartList)
                .build();

            given(cartRepository.findByMemberId(any())).willReturn(Optional.of(cart));

            // when
            FindCartResponse findCartResponse = roomCartService.getCartList(1L);

            // then
            assertThat(findCartResponse).isNotNull();
            assertThat(findCartResponse.getAccommodationList().size()).isGreaterThan(0);
        }
    }
}

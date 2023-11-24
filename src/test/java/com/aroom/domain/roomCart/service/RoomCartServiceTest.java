package com.aroom.domain.roomCart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.repository.RoomRepository;
import com.aroom.domain.roomCart.dto.response.RoomCartInfoDTO;
import com.aroom.domain.roomCart.dto.response.RoomCartResponseDTO;
import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @Nested
    @DisplayName("postRoomCart()은")
    class Context_postRoomCart {

        @Test
        @DisplayName("장바구니에 객실을 등록할 수 있다.")
        void _willSuccess() {
            // given
            Room room = Room.builder().type("premium").price(50000).stock(4).id(1L).build();
            Member member = Member.builder().email("yang980329@naver.com").build();
            Cart cart = Cart.builder().id(1L).member(member).roomCartList(new ArrayList<>())
                .build();
            RoomCart roomCart = RoomCart.builder().cart(cart).room(room).build();
            given(roomRepository.findById(any(Long.TYPE))).willReturn(Optional.of(room));
            given(cartRepository.findByMemberId(any(Long.TYPE))).willReturn(
                Optional.of(cart));
            given(roomCartRepository.save(any(RoomCart.class))).willReturn(roomCart);

            // when
            RoomCartResponseDTO roomCartResponseDTO = roomCartService.postRoomCart(1L, 1L);

            // then
            assertNotNull(roomCartResponseDTO);
            assertEquals(roomCartResponseDTO.getRoomCartList().get(0).getCart_id(),
                cart.getRoomCartList().get(0).getCart().getId());
        }

        @Test
        @DisplayName("숙소 정보를 찾을 수 없다면, 상세 조회할 수 없다.")
        void outOfStockException_willFail() {
            // given
            Room room = Room.builder().type("premium").price(50000).stock(0).id(1L).build();
            Member member = Member.builder().email("yang980329@naver.com").build();
            Cart cart = Cart.builder().id(1L).member(member).roomCartList(new ArrayList<>())
                .build();
            given(roomRepository.findById(any(Long.TYPE))).willReturn(Optional.of(room));
            given(cartRepository.findByMemberId(any(Long.TYPE))).willReturn(
                Optional.of(cart));

            // when
            Throwable exception = assertThrows(OutOfStockException.class, () -> {
                roomCartService.postRoomCart(1L, 1L);
            });

            // then
            assertEquals("상품의 재고 부족으로 장바구니 담기가 불가합니다.", exception.getMessage());
        }
    }
}

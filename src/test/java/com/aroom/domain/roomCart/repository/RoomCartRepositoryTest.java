package com.aroom.domain.roomCart.repository;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.repository.RoomRepository;
import com.aroom.domain.roomCart.model.RoomCart;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RoomCartRepositoryTest {

    @Autowired
    private RoomCartRepository roomCartRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CartRepository cartRepository;

    @Test
    @DisplayName("성공적으로 장바구니에 등록했습니다.")
    void save() {
        // given
        Room room = Room.builder().type("premium").price(50000).build();
        Member member = Member.builder().email("yang980329@naver.com").build();
        Cart cart = Cart.builder().member(member).roomCartList(new ArrayList<>()).build();
        RoomCart roomCart = RoomCart.builder().cart(cart).room(room).build();

        // when
        RoomCart savedRoomCart = roomCartRepository.save(roomCart);

        // then
        assertThat(savedRoomCart.getCart().getMember().getEmail()).isEqualTo("yang980329@naver.com");
        assertThat(savedRoomCart.getRoom().getType()).isEqualTo("premium");
    }
}

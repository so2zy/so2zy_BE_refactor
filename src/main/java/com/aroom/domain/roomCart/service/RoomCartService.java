package com.aroom.domain.roomCart.service;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.domain.roomProduct.repository.RoomProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomCartService {

    private final RoomProductRepository roomProductRepository;
    private final CartRepository cartRepository;
    private final RoomCartRepository roomCartRepository;

    public RoomCartResponse postRoomCart(Long member_id, Long room_id) {
        RoomProduct roomProduct = roomProductRepository.findByRoomId(room_id).get();
        Cart cart = cartRepository.findByMemberId(member_id).get();
        RoomCart roomCart = RoomCart.builder().cart(cart).roomProduct(roomProduct).build();
        roomCartRepository.save(roomCart);
        cart.postRoomCarts(roomCart);
        return new RoomCartResponse(cart);
    }
}

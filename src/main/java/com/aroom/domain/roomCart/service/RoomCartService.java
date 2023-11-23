package com.aroom.domain.roomCart.service;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.repository.RoomRepository;
import com.aroom.domain.roomCart.dto.response.RoomCartResponseDTO;
import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomCartService {

    private final RoomRepository roomRepository;
    private final CartRepository cartRepository;
    private final RoomCartRepository roomCartRepository;

    public RoomCartResponseDTO postRoomCart(Long member_id, Long room_id) {
        Room room = roomRepository.findById(room_id).get();
        Cart cart = cartRepository.findByMemberId(member_id).get();
        if (room.getStock() > 0) {
            room.updateRoomStock(room.getStock() - 1);
            RoomCart roomCart = roomCartRepository.save(new RoomCart(cart, room));
            cart.postRoomCarts(roomCart);
            return new RoomCartResponseDTO(cart);
        } else {
            throw new OutOfStockException();
        }
    }
}

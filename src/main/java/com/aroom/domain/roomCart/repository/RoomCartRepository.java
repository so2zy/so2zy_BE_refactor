package com.aroom.domain.roomCart.repository;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.roomCart.model.RoomCart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCartRepository extends JpaRepository<RoomCart, Long> {

    void deleteByCart(Cart cart);

    List<RoomCart> findByRoomProductId(long roomProductId);
}

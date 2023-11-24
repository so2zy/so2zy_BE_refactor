package com.aroom.domain.cart.repository;

import com.aroom.domain.cart.model.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberId(Long member_id);

}

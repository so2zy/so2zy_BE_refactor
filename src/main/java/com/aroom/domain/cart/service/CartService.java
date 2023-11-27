package com.aroom.domain.cart.service;

import com.aroom.domain.cart.dto.response.FindCartResponse;
import com.aroom.domain.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    public FindCartResponse getCartList(Long memberId) {
        return null;
    }
}

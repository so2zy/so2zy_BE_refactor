package com.aroom.domain.roomCart.service;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.roomCart.dto.request.RoomCartRequest;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import com.aroom.domain.roomProduct.exception.RoomProductNotFoundException;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.domain.roomProduct.repository.RoomProductRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomCartService {

    private final RoomProductRepository roomProductRepository;
    private final CartRepository cartRepository;
    private final RoomCartRepository roomCartRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RoomCartResponse postRoomCart(Long member_id, Long room_id,
        RoomCartRequest roomCartRequest) {

        Optional<Cart> optionalCart = cartRepository.findByMemberId(member_id);
        Cart cart = optionalCart.orElseGet(() -> {
            Member member = memberRepository.findById(member_id).orElseThrow(
                MemberNotFoundException::new);
            return cartRepository.save(new Cart(member));
        });

        List<RoomProduct> roomProductList = roomProductRepository.findByRoomIdAndStartDateAndEndDate(
            room_id,
            roomCartRequest.getStartDate(), roomCartRequest.getEndDate());

        checkContinualDate(roomProductList, roomCartRequest);

        RoomProduct roomProductMinStock = findMinStockRoomProduct(roomProductList);

        for (RoomProduct roomProduct : roomProductList) {
            List<RoomCart> roomCartList = roomCartRepository.findByRoomProductId(
                roomProductMinStock.getId());
            if (roomProduct.getStock() - roomCartList.size() > 0) {
                RoomCart roomCart = RoomCart.builder().cart(cart).roomProduct(roomProduct).build();
                roomCartRepository.save(roomCart);
                cart.postRoomCarts(roomCart);
            } else {
                throw new OutOfStockException();
            }
        }
        return new RoomCartResponse(cart);
    }

    private void checkContinualDate(List<RoomProduct> roomProductList,
        RoomCartRequest roomCartRequest) {
        LocalDate startDate = roomCartRequest.getStartDate();
        LocalDate endDate = roomCartRequest.getEndDate();
        long betweenDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (roomProductList.size() != betweenDays) {
            throw new RoomProductNotFoundException();
        }
    }

    private RoomProduct findMinStockRoomProduct(List<RoomProduct> roomProductList) {
        int minStock = Integer.MAX_VALUE;
        for (RoomProduct roomProduct : roomProductList) {
            minStock = Math.min(roomProduct.getStock(), minStock);
        }
        RoomProduct minStockRoomProduct = roomProductRepository.findByStock(minStock).get();
        return minStockRoomProduct;
    }
}

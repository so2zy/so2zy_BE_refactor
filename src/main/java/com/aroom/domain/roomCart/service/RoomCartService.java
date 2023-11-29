package com.aroom.domain.roomCart.service;

import com.aroom.domain.accommodation.dto.response.CartAccommodationResponse;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.cart.exception.CartNotFoundException;
import com.aroom.domain.roomCart.dto.response.FindCartResponse;
import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.room.dto.response.CartRoomResponse;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.roomCart.dto.request.RoomCartRequest;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import com.aroom.domain.roomProduct.exception.RoomProductNotFoundException;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.domain.roomProduct.repository.RoomProductRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Transactional
    public FindCartResponse getCartList(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId)
            .orElseThrow(CartNotFoundException::new);

        List<RoomCart> roomCartList = cart.getRoomCartList();
        return createResponse(roomCartList);
    }

    private FindCartResponse createResponse(List<RoomCart> roomCartList){
        Map<Long, List<RoomProduct>> roomProductListMap = createRoomProductListMap(roomCartList);

        List<CartAccommodationResponse> cartAccommodationList = new ArrayList<>();
        for (Long id : roomProductListMap.keySet()) {
            List<RoomProduct> roomProductList = roomProductListMap.get(id);
            sortRoomProductListByDate(roomProductList);

            List<CartRoomResponse> cartRoomList = createCartRoomList(roomProductList);

            Accommodation accommodation = roomProductList.get(0).getRoom().getAccommodation();

            CartAccommodationResponse cartAccommodation = CartAccommodationResponse.builder()
                .accommodationId(accommodation.getId())
                .accommodationName(accommodation.getName())
                .address(accommodation.getAddress())
                .roomList(cartRoomList)
                .build();

            cartAccommodationList.add(cartAccommodation);
        }

        return FindCartResponse.builder()
            .accommodationList(cartAccommodationList)
            .build();
    }

    private Map<Long, List<RoomProduct>> createRoomProductListMap(List<RoomCart> roomCartList){
        Map<Long, List<RoomProduct>> roomProductListMap = new HashMap<>();

        for (RoomCart roomCart : roomCartList) {
            Room room = roomCart.getRoomProduct().getRoom();
            roomProductListMap.put(room.getId(), new ArrayList<>());
        }

        for (RoomCart roomCart : roomCartList) {
            RoomProduct roomProduct = roomCart.getRoomProduct();
            Room room = roomProduct.getRoom();

            List<RoomProduct> roomProductList = roomProductListMap.get(room.getId());
            roomProductList.add(roomProduct);
        }

        return roomProductListMap;
    }

    private void sortRoomProductListByDate(List<RoomProduct> roomProductList){
        roomProductList.sort(new Comparator<RoomProduct>() {
            @Override
            public int compare(RoomProduct o1, RoomProduct o2) {
                if(o1.getStartDate().isBefore(o2.getStartDate())){
                    return 1;
                }else if(o1.getStartDate().isAfter(o2.getStartDate())){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }

    private List<CartRoomResponse> createCartRoomList(List<RoomProduct> roomProductList){
        List<CartRoomResponse> cartRoomList = new ArrayList<>();

        LocalDate preDate = roomProductList.get(0).getStartDate();
        LocalDate startDate = roomProductList.get(0).getStartDate();

        for (int i = 0; i < roomProductList.size();i++) {
            RoomProduct roomProduct = roomProductList.get(i);

            if (preDate.plusDays(1).isEqual(roomProduct.getStartDate())
                && i < roomProductList.size() - 1) {
                preDate = roomProduct.getStartDate();
            } else {
                cartRoomList.add(CartRoomResponse.builder()
                    .roomId(roomProduct.getRoom().getId())
                    .type(roomProduct.getRoom().getType())
                    .checkIn(roomProduct.getRoom().getCheckIn())
                    .checkOut(roomProduct.getRoom().getCheckOut())
                    .capacity(roomProduct.getRoom().getCapacity())
                    .maxCapacity(roomProduct.getRoom().getMaxCapacity())
                    .price(roomProduct.getRoom().getPrice())
                    .startDate(startDate)
                    .endDate(startDate.plusDays(1))
                    .roomImageUrl(roomProduct.getRoom().getRoomImageList().get(0).getUrl())
                    .build());

                startDate = roomProduct.getStartDate();
            }
        }

        return cartRoomList;
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

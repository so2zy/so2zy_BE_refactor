package com.aroom.domain.roomCart.service;

import com.aroom.domain.accommodation.dto.response.CartAccommodationResponse;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.cart.dto.response.FindCartResponse;
import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.room.dto.response.CartRoomResponse;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.domain.roomProduct.repository.RoomProductRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final MemberRepository memberRepository;

    public RoomCartResponse postRoomCart(Long member_id, Long room_id) {
        RoomProduct roomProduct = roomProductRepository.findByRoomId(room_id).get();
        Cart cart = cartRepository.findByMemberId(member_id).get();
        RoomCart roomCart = RoomCart.builder().cart(cart).roomProduct(roomProduct).build();
        roomCartRepository.save(roomCart);
        cart.postRoomCarts(roomCart);
        return new RoomCartResponse(cart);
    }

    public FindCartResponse getCartList(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        List<RoomCart> roomCartList = member.getCart().getRoomCartList();
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

        for (int i = 1; i < roomProductList.size();i++) {
            RoomProduct roomProduct = roomProductList.get(i);

            if (preDate.plusDays(1).isEqual(roomProduct.getStartDate())
                && i < roomProductList.size() - 1) {
                // 연속 되고 마지막 아닐 때.
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
}

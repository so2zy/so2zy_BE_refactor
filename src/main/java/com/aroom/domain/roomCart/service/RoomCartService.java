package com.aroom.domain.roomCart.service;

import com.aroom.domain.accommodation.dto.response.CartAccommodationResponse;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.room.dto.response.CartRoomResponse;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.roomCart.dto.request.RoomCartRequest;
import com.aroom.domain.roomCart.dto.response.FindCartResponse;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.exception.OutOfStockException;
import com.aroom.domain.roomCart.exception.WrongDateException;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomCart.repository.RoomCartRepository;
import com.aroom.domain.roomProduct.exception.RoomProductNotFoundException;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.domain.roomProduct.repository.RoomProductRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        checkStartDateEndDate(roomCartRequest);

        Optional<Cart> optionalCart = cartRepository.findByMemberId(member_id);
        Cart cart = getOrCreateCart(optionalCart, member_id);

        List<RoomProduct> roomProductList = new ArrayList<>();
        if (roomCartRequest.getStartDate().equals(roomCartRequest.getEndDate().minusDays(1))) {
            RoomProduct roomProduct = roomProductRepository.findByRoomIdAndStartDate(
                    room_id, roomCartRequest.getStartDate())
                .orElseThrow(RoomProductNotFoundException::new);
            List<RoomCart> roomCartList = roomCartRepository.findByRoomProductId(
                roomProduct.getId());
            if (roomProduct.getStock() - roomCartList.size() > 0) {
                RoomCart roomCart = RoomCart.builder().cart(cart).roomProduct(roomProduct)
                    .personnel(roomCartRequest.getPersonnel()).build();
                roomCartRepository.save(roomCart);
                cart.postRoomCarts(roomCart);
                return new RoomCartResponse(cart);
            } else {
                throw new OutOfStockException();
            }
        } else {
            roomProductList = roomProductRepository.findByRoomIdAndStartDateAndEndDate(
                room_id,
                roomCartRequest.getStartDate(), roomCartRequest.getEndDate().minusDays(1));
            checkContinualDate(roomProductList, roomCartRequest);

            RoomProduct roomProductMinStock = findMinStockRoomProduct(roomProductList);

            for (RoomProduct roomProduct : roomProductList) {
                List<RoomCart> roomCartList = roomCartRepository.findByRoomProductId(
                    roomProductMinStock.getId());
                if (roomProduct.getStock() - roomCartList.size() > 0) {
                    RoomCart roomCart = RoomCart.builder().cart(cart).roomProduct(roomProduct)
                        .personnel(roomCartRequest.getPersonnel()).build();
                    roomCartRepository.save(roomCart);
                    cart.postRoomCarts(roomCart);
                } else {
                    throw new OutOfStockException();
                }
            }
            return new RoomCartResponse(cart);
        }

    }

    @Transactional
    public FindCartResponse getCartList(Long memberId) {
        Optional<Cart> optionalCart = cartRepository.findByMemberId(memberId);
        Cart cart = getOrCreateCart(optionalCart, memberId);

        List<RoomCart> roomCartList = cart.getRoomCartList();

        return createResponse(roomCartList);
    }


    private Cart getOrCreateCart(Optional<Cart> optionalCart, Long memberId) {
        return optionalCart.orElseGet(() -> {
            Member member = memberRepository.findById(memberId).orElseThrow(
                MemberNotFoundException::new);
            return cartRepository.save(new Cart(member));
        });
    }

    private FindCartResponse createResponse(List<RoomCart> roomCartList) {
        Map<Long, List<RoomProduct>> roomProductListMap = createRoomProductListMap(roomCartList);

        List<Integer> personnelList = new ArrayList<>();
        for (RoomCart roomCart : roomCartList) {
            personnelList.add(roomCart.getPersonnel());
        }

        List<CartAccommodationResponse> cartAccommodationList = new ArrayList<>();
        for (Long id : roomProductListMap.keySet()) {
            List<RoomProduct> roomProductList = roomProductListMap.get(id);

            List<CartRoomResponse> cartRoomList = createCartRoomList(roomProductList,
                personnelList);

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

    private Map<Long, List<RoomProduct>> createRoomProductListMap(List<RoomCart> roomCartList) {
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

    private List<CartRoomResponse> createCartRoomList(List<RoomProduct> roomProductList,
        List<Integer> personnelList) {
        List<CartRoomResponse> cartRoomList = new ArrayList<>();

        LocalDate preDate = roomProductList.get(0).getStartDate();
        LocalDate startDate = roomProductList.get(0).getStartDate();

        RoomProduct roomProduct = null;
        for (int i = 0; i < roomProductList.size(); i++) {
            roomProduct = roomProductList.get(i);

            if (preDate.plusDays(1).isEqual(roomProduct.getStartDate()) || i == 0) {
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
                    .endDate(preDate.plusDays(1))
                    .roomImageUrl(roomProduct.getRoom().getRoomImageList().get(0).getUrl())
                    .personnel(personnelList.get(i))
                    .build());

                startDate = roomProduct.getStartDate();
                preDate = startDate;
            }
        }

        cartRoomList.add(CartRoomResponse.builder()
            .roomId(roomProduct.getRoom().getId())
            .type(roomProduct.getRoom().getType())
            .checkIn(roomProduct.getRoom().getCheckIn())
            .checkOut(roomProduct.getRoom().getCheckOut())
            .capacity(roomProduct.getRoom().getCapacity())
            .maxCapacity(roomProduct.getRoom().getMaxCapacity())
            .price(roomProduct.getRoom().getPrice())
            .startDate(startDate)
            .endDate(preDate.plusDays(1))
            .roomImageUrl(roomProduct.getRoom().getRoomImageList().get(0).getUrl())
            .personnel(personnelList.get(personnelList.size() - 1))
            .build());

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
        roomProductList.sort(new Comparator<RoomProduct>() {
            @Override
            public int compare(RoomProduct o1, RoomProduct o2) {
                if (o1.getStock() > o2.getStock()) {
                    return 1;
                } else if (o1.getStock() < o2.getStock()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return roomProductList.get(0);
    }

    private void checkStartDateEndDate(RoomCartRequest roomCartRequest) {
        LocalDate startDate = roomCartRequest.getStartDate();
        LocalDate endDate = roomCartRequest.getEndDate();
        if (startDate.isAfter(endDate)) {
            throw new WrongDateException();
        }
    }
}

package com.aroom.domain.cart.service;

import com.aroom.domain.accommodation.dto.response.CartAccommodationResponse;
import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.cart.dto.response.FindCartResponse;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.room.dto.response.CartRoomResponse;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.roomCart.model.RoomCart;
import com.aroom.domain.roomProduct.model.RoomProduct;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    public FindCartResponse getCartList(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        List<RoomCart> roomCartList = member.getCart().getRoomCartList();

        for (RoomCart roomCart : roomCartList) {
            RoomProduct roomProduct = roomCart.getRoomProduct();
            Room room = roomProduct.getRoom();
            Accommodation accommodation = room.getAccommodation();
            CartRoomResponse.builder()
                    .roomId(room.getId())
                        .type(room.getType())
                            .checkIn(room.getCheckIn())
                                .checkOut(room.getCheckOut())
                                    .capacity(room.getCapacity())
                                        .maxCapacity(room.getMaxCapacity())
                                            .price(room.getPrice())
                                                .startDate()

            CartAccommodationResponse.builder()
                .accommodationId(accommodation.getId())
                .accommodationName(accommodation.getName())
                .address(accommodation.getAddressCode())
                .roomList()
        }


        return null;
    }

    private void getStartDateAndEndDate(RoomProduct roomProduct){
        // 가장 빠른 날짜 늦는 날짜 하나씩.
        LocalDate startDate = roomProduct.getStartDate();

    }
}

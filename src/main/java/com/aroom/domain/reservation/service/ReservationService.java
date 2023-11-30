package com.aroom.domain.reservation.service;

import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;
import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.dto.response.ReservationResponse;
import com.aroom.domain.reservation.exception.MaximumCapacityExceededException;
import com.aroom.domain.reservation.exception.OutOfStockException;
import com.aroom.domain.reservation.exception.ReservationErrorCode;
import com.aroom.domain.reservation.model.Reservation;
import com.aroom.domain.reservation.repository.ReservationRepository;
import com.aroom.domain.reservationRoom.model.ReservationRoom;
import com.aroom.domain.reservationRoom.repository.ReservationRoomRepository;
import com.aroom.domain.room.dto.request.RoomReservationRequest;
import com.aroom.domain.room.dto.response.RoomReservationResponse;
import com.aroom.domain.room.exception.RoomNotFoundException;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import com.aroom.domain.room.repository.RoomImageRepository;
import com.aroom.domain.room.repository.RoomRepository;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.domain.roomProduct.repository.RoomProductRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRoomRepository reservationRoomRepository;
    private final ReservationRepository reservationRepository;
    private final RoomImageRepository roomImageRepository;
    private final MemberRepository memberRepository;
    private final RoomProductRepository roomProductRepository;

    @Transactional
    public ReservationResponse reserveRoom(ReservationRequest request, Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
            MemberNotFoundException::new);

        Reservation reservation = Reservation.builder()
            .member(member)
            .agreement(request.getAgreement())
            .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        List<RoomReservationResponse> responseRoomList = new ArrayList<>();

        for (RoomReservationRequest roomRequest : request.getRoomList()) {
            Room requiredRoom = roomRepository.findById(roomRequest.getRoomId())
                .orElseThrow(() -> new OutOfStockException("해당 방이 존재하지 않습니다.",
                ReservationErrorCode.OUT_OF_STOCK_ERROR));

            checkCapacityOfRoom(roomRequest.getPersonnel(), requiredRoom);

            requiredRoom.addSoldCount();

            List<RoomProduct> roomProductList = roomProductRepository.findByRoomAndBetweenStartDateAndEndDate(
                requiredRoom, roomRequest.getStartDate(), roomRequest.getEndDate().minusDays(1));

            checkOverlappingReservation(roomProductList);

            Long reservationRoomId = 0L;
            for (RoomProduct roomProduct : roomProductList) {
                roomProduct.sellRoomProduct();

                ReservationRoom reservationRoom = ReservationRoom.builder()
                    .roomProduct(roomProduct)
                    .reservation(reservation)
                    .startDate(roomRequest.getStartDate())
                    .endDate(roomRequest.getEndDate())
                    .price(roomRequest.getPrice())
                    .personnel(roomRequest.getPersonnel())
                    .build();

                ReservationRoom savedReservationRoom = reservationRoomRepository.save(
                    reservationRoom);
                reservationRoomId = savedReservationRoom.getId();
            }

            Optional<RoomImage> optionalRoomImage = roomImageRepository.findByRoomId(requiredRoom.getId());
            RoomImage roomImage = optionalRoomImage.orElseGet(() -> {
                return RoomImage.builder().url("").build();
            });

            RoomReservationResponse responseRoom = RoomReservationResponse.builder()
                .roomId(requiredRoom.getId())
                .type(requiredRoom.getType())
                .checkIn(requiredRoom.getCheckIn())
                .checkOut(requiredRoom.getCheckOut())
                .capacity(requiredRoom.getCapacity())
                .maxCapacity(requiredRoom.getMaxCapacity())
                .price(requiredRoom.getPrice())
                .startDate(roomRequest.getStartDate())
                .endDate(roomRequest.getEndDate())
                .roomImageUrl(roomImage.getUrl())
                .roomReservationNumber(reservationRoomId)
                .build();

            responseRoomList.add(responseRoom);
        }

        if (request.isFromCart()) {
            member.getCart().clearCart();
        }

        return ReservationResponse.builder()
            .roomList(responseRoomList)
            .reservationNumber(savedReservation.getId())
            .dealDateTime(LocalDateTime.now())
            .build();
    }

    private void checkOverlappingReservation(List<RoomProduct> roomProductList) {
        for (RoomProduct roomProduct : roomProductList) {
            if (roomProduct.getStock() < 1) {
                throw new OutOfStockException("방이 품절 되었습니다.",
                    ReservationErrorCode.OUT_OF_STOCK_ERROR);
            }
        }
    }

    private void checkCapacityOfRoom(int personnel, Room requiredRoom) {
        if (requiredRoom.getMaxCapacity() < personnel) {
            throw new MaximumCapacityExceededException("예약 최대 인원을 초과합니다.",
                ReservationErrorCode.MAXIMUM_CAPACITY_EXCEEDED);
        }
    }

}

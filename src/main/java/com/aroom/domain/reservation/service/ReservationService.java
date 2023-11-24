package com.aroom.domain.reservation.service;

import com.aroom.domain.member.model.Member;
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
import com.aroom.domain.room.dto.response.RoomImageResponse;
import com.aroom.domain.room.dto.response.RoomReservationResponse;
import com.aroom.domain.room.model.Room;
import com.aroom.domain.room.model.RoomImage;
import com.aroom.domain.room.repository.RoomImageRepository;
import com.aroom.domain.room.repository.RoomRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Transactional
    public ReservationResponse reserveRoom(ReservationRequest request, Member member) {
        Reservation reservation = Reservation.builder()
            .member(member)
            .agreement(request.getAgreement())
            .build();

        List<RoomReservationResponse> responseRoomList = new ArrayList<>();

        ReservationRoom reservationRoom = ReservationRoom.builder().build();
        for (RoomReservationRequest roomRequest : request.getRoomList()) {
            Room requiredRoom = roomRepository.findByIdWithLock(roomRequest.getRoomId())
                .orElseThrow(RuntimeException::new);

            checkOverlappingReservation(roomRequest, requiredRoom);
            checkCapacityOfRoom(request.getPersonnel(), requiredRoom);

            reservationRoom = ReservationRoom.builder()
                .room(requiredRoom)
                .reservation(reservation)
                .startDate(roomRequest.getStartDate())
                .endDate(roomRequest.getEndDate())
                .price(roomRequest.getPrice())
                .personnel(request.getPersonnel())
                .build();


            RoomImage roomImage = roomImageRepository.findById(
                requiredRoom.getAccommodation().getId()).orElseThrow(RuntimeException::new);

            RoomImageResponse imageResponse = new RoomImageResponse(roomImage.getUrl());

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
                .roomImage(imageResponse)
                .build();

            responseRoomList.add(responseRoom);
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        ReservationRoom savedReservationRoom = reservationRoomRepository.save(reservationRoom);

        if(request.isFromCart()){
            member.getCart().clearCart();
        }

        return ReservationResponse.builder()
            .roomList(responseRoomList)
            .roomReservationNumber(savedReservationRoom.getId())
            .reservationNumber(savedReservation.getId())
            .dealDateTime(LocalDateTime.now())
            .build();
    }

    private void checkOverlappingReservation(RoomReservationRequest requestRoom, Room requiredRoom) {
        int reservedRoomCount = reservationRoomRepository.getOverlappingReservationByDateRange(
            requiredRoom, requestRoom.getStartDate().plusDays(1), requestRoom.getEndDate().plusDays(1));

        if(reservedRoomCount < requiredRoom.getStock()){
            return;
        }
        throw new OutOfStockException("방이 품절 되었습니다.", ReservationErrorCode.OUT_OF_STOCK_ERROR);
    }

    private void checkCapacityOfRoom(int personnel, Room requiredRoom){
        if(requiredRoom.getMaxCapacity() < personnel){
            throw new MaximumCapacityExceededException("예약 최대 인원을 초과합니다.",
                ReservationErrorCode.MAXIMUM_CAPACITY_EXCEEDED);
        }
    }

}

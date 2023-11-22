package com.aroom.domain.reservation.service;

import com.aroom.domain.member.model.Member;
import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.dto.response.ReservationResponse;
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
import com.aroom.domain.roomCart.repository.RoomCartRepository;
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
    private final RoomCartRepository roomCartRepository;

    @Transactional
    public ReservationResponse reserveRoom(ReservationRequest request, Member member) {
        Reservation reservation = Reservation.builder()
            .member(member)
            .agreement(request.getAgreement())
            .build();

        List<RoomReservationResponse> responseRoomList = new ArrayList<>();

        ReservationRoom reservationRoom = null;
        for (RoomReservationRequest roomRequest : request.getRoomList()) {
            Room requiredRoom = roomRepository.findById(roomRequest.getRoomId())
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

            reservationRoomRepository.save(reservationRoom);

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

        reservationRepository.save(reservation);

        if(request.isFromCart()){
            roomCartRepository.deleteByCart(member.getCart());
        }

        assert reservationRoom != null;
        return ReservationResponse.builder()
            .roomList(responseRoomList)
            .roomReservationNumber(reservationRoom.getId())
            .reservationNumber(reservation.getId())
            .dealDateTime(LocalDateTime.now())
            .build();
    }

    private void checkOverlappingReservation(RoomReservationRequest requestRoom, Room requiredRoom) {
        int reservedRoomCount = reservationRoomRepository.getOverlappingReservationByDateRange(
            requiredRoom, requestRoom.getStartDate().plusDays(1), requestRoom.getEndDate().plusDays(1));

        if(reservedRoomCount < requiredRoom.getStock()){
            return;
        }
        throw new RuntimeException("품절된 방입니다.");
    }

    private void checkCapacityOfRoom(int personnel, Room requiredRoom){
        if(requiredRoom.getMaxCapacity() < personnel){
            throw new RuntimeException("인원을 초과합니다.");
        }
    }

}

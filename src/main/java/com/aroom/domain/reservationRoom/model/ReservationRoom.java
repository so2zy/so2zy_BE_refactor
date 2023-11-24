package com.aroom.domain.reservationRoom.model;

import com.aroom.domain.reservation.model.Reservation;
import com.aroom.domain.room.model.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Reservation reservation;

    private LocalDate startDate;

    private LocalDate endDate;

    private int price;

    private int personnel;

    @Builder
    public ReservationRoom(Room room, Reservation reservation, LocalDate startDate,
        LocalDate endDate, int price, int personnel) {
        this.room = room;
        this.reservation = reservation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.personnel = personnel;
    }
}

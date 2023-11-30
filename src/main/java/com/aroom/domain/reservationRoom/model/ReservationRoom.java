package com.aroom.domain.reservationRoom.model;

import com.aroom.domain.reservation.model.Reservation;
import com.aroom.domain.roomProduct.model.RoomProduct;
import com.aroom.global.basetime.BaseTimeEntity;
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
public class ReservationRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_room_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "room_product_id", nullable = false)
    private RoomProduct roomProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int personnel;

    @Builder
    public ReservationRoom(Long id, RoomProduct roomProduct, Reservation reservation,
        LocalDate startDate, LocalDate endDate, int price, int personnel) {
        this.id = id;
        this.roomProduct = roomProduct;
        this.reservation = reservation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.personnel = personnel;
    }
}

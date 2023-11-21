package com.aroom.domain.order_room.model;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.domain.order.model.Order;
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
public class OrderRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private LocalDate startDate;

    private LocalDate endDate;

    private int price;

    private int personnel;

    @Builder
    public OrderRoom(Accommodation accommodation, Order order, LocalDate startDate,
        LocalDate endDate,
        int price, int personnel) {
        this.accommodation = accommodation;
        this.order = order;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.personnel = personnel;
    }
}

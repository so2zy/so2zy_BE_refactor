package com.aroom.domain.room.model;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.global.basetime.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private LocalDateTime checkIn;

    @Column(nullable = false)
    private LocalDateTime checkOut;

    @Column(nullable = false)
    private int stock;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomImage> roomImageList = new ArrayList<>();

    @Builder
    public Room(Accommodation accommodation, String type, int price, int capacity, int maxCapacity,
        LocalDateTime checkIn, LocalDateTime checkOut, int stock, List<RoomImage> roomImageList) {
        this.accommodation = accommodation;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.maxCapacity = maxCapacity;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.stock = stock;
        this.roomImageList = roomImageList;
    }
}

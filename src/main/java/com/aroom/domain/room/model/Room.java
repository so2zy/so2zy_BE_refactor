package com.aroom.domain.room.model;

import com.aroom.domain.accommodation.model.Accommodation;
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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    private String type;

    private int price;

    private int capacity;

    private int maxCapacity;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

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
package com.aroom.domain.roomProduct.model;

import com.aroom.domain.room.model.Room;
import com.aroom.global.basetime.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_product_id")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private LocalDate startDate;

    @Builder
    public RoomProduct(Long id, Room room, Integer stock, LocalDate startDate) {
        this.id = id;
        this.room = room;
        this.stock = stock;
        this.startDate = startDate;
    }

    public void sellRoomProduct(){
        this.stock--;
    }
}

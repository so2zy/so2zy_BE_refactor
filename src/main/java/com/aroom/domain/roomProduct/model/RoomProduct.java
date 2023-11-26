package com.aroom.domain.roomProduct.model;

import com.aroom.domain.room.model.Room;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class RoomProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_product_id")
    private Long id;

    @OneToMany(mappedBy = "room")
    @JsonManagedReference
    @Column(nullable = false)
    private List<Room> roomList = new ArrayList<>();

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private LocalDate startDate;

}

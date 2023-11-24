package com.aroom.domain.accommodation.model;

import com.aroom.domain.room.model.Room;
import com.aroom.global.basetime.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;

    @Column(nullable = false)
    private String addressCode;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private String phoneNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY)
    private List<Room> roomList = new ArrayList<>();

    @OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY)
    private List<AccommodationImage> accommodationImageList = new ArrayList<>();

    @Builder
    public Accommodation(String name, float latitude, float longitude, String addressCode,
        int likeCount, String phoneNumber, List<Room> roomList,
        List<AccommodationImage> accommodationImageList) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressCode = addressCode;
        this.likeCount = likeCount;
        this.phoneNumber = phoneNumber;
        this.roomList = roomList;
        this.accommodationImageList = accommodationImageList;
    }
}

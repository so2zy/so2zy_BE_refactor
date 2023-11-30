package com.aroom.domain.address.model;

import com.aroom.domain.accommodation.model.Accommodation;
import com.aroom.global.basetime.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private Integer areaCode;

    @Column(nullable = false)
    private Integer sigunguCode;

    @Column(nullable = false)
    private String areaName;

    @Column(nullable = false)
    private String sigunguName;

    @Builder
    public Address(Long id, Integer areaCode, Integer sigunguCode, String areaName,
        String sigunguName) {
        this.id = id;
        this.areaCode = areaCode;
        this.sigunguCode = sigunguCode;
        this.areaName = areaName;
        this.sigunguName = sigunguName;
    }
}

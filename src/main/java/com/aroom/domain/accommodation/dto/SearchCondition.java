package com.aroom.domain.accommodation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchCondition {

    private String name;
    private String areaName;
    private String sigunguName;
    private String likeCount;
    private String phoneNumber;
    private String lowestPrice;
    private String highestPrice;
    private Integer capacity;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime checkIn;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime checkOut;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String orderBy;
    private String orderCondition;

}

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalTime checkIn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalTime checkOut;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer areaCode;
    private Integer sigunguCode;

    private String orderBy;
    private String orderCondition;

}

package com.aroom.domain.accommodation.dto;

import java.time.LocalDateTime;
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
    private String addressCode;
    private String likeCount;
    private String phoneNumber;
    private String lowestPrice;
    private String highestPrice;
    private Integer capacity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalTime checkIn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalTime checkOut;

    private String orderBy;
    private String orderCondition;

}

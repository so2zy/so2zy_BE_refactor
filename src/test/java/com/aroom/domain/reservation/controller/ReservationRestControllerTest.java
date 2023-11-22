package com.aroom.domain.reservation.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.dto.response.ReservationResponse;
import com.aroom.domain.reservation.service.ReservationService;
import com.aroom.domain.room.dto.request.ReservationRoomRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ReservationRestController.class)
class ReservationRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationService reservationService;

    private ReservationResponse reservationResponse;
    private ReservationRoomRequest roomRequest;

    @BeforeEach
    private void init(){
        reservationResponse = ReservationResponse.builder()
            .roomId(1L)
            .roomType("패밀리")
            .checkIn(LocalTime.of(15, 00))
            .checkOut(LocalTime.of(18, 00))
            .capacity(2)
            .maxCapacity(4)
            .roomReservationNumber(2L)
            .reservationNumber(3L)
            .dealDateTime(LocalDateTime.now())
            .build();

        roomRequest = ReservationRoomRequest.builder()
            .roomId(1L)
            .startDate(LocalDate.of(2023, 12, 22))
            .endDate(LocalDate.of(2023, 12, 23))
            .build();
    }

    @Test
    @DisplayName("하나의 숙소의 하나의 객실을 예약에 성공")
    void reserve_one_room_of_one_accommodation_success() throws Exception{
        ReservationRequest request = ReservationRequest.builder()
            .roomList(List.of(roomRequest))
            .personnel(2)
            .agreement(true)
            .build();
        given(reservationService.reserveRoom(any())).willReturn(reservationResponse);

        ResultActions response = mvc.perform(post("/v1/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsBytes(request)));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.data.roomType", is(reservationResponse.getRoomType())));
    }

    @Test
    @DisplayName("객실 예약 데이터 중 객실 데이터가 없는 경우")
    void reserve_no_room_data_fail() throws Exception{
        ReservationRequest request = ReservationRequest.builder()
            .personnel(2)
            .agreement(true)
            .build();
        given(reservationService.reserveRoom(any())).willReturn(reservationResponse);

        ResultActions response = mvc.perform(post("/v1/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsBytes(request)));

        response.andExpect(status().isBadRequest())
            .andDo(print());
    }

}
package com.aroom.domain.reservation.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.reservation.dto.request.ReservationRequest;
import com.aroom.domain.reservation.dto.response.ReservationResponse;
import com.aroom.domain.reservation.service.ReservationService;
import com.aroom.domain.room.dto.request.RoomReservationRequest;
import com.aroom.domain.room.dto.response.RoomReservationResponse;
import com.aroom.util.security.WithMockAccountContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ReservationRestController.class)
class ReservationRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationService reservationService;

    private ReservationResponse reservationResponse;
    private RoomReservationRequest roomRequest;


    @BeforeEach
    private void init() {
        roomRequest = RoomReservationRequest.builder()
            .roomId(1L)
            .startDate(LocalDate.of(2023, 12, 22))
            .endDate(LocalDate.of(2023, 12, 23))
            .personnel(2)
            .build();

        RoomReservationResponse roomResponse = RoomReservationResponse.builder()
            .roomId(1L)
            .type("패밀리")
            .checkIn(LocalTime.of(19, 0))
            .checkOut(LocalTime.of(13, 0))
            .capacity(2)
            .maxCapacity(4)
            .price(100000)
            .startDate(roomRequest.getStartDate())
            .endDate(roomRequest.getEndDate())
            .roomImageUrl("testimage.com")
            .roomReservationNumber(1L)
            .build();

        reservationResponse = ReservationResponse.builder()
            .roomList(List.of(roomResponse))
            .reservationNumber(3L)
            .dealDateTime(LocalDateTime.now())
            .build();
    }

    @Nested
    @DisplayName("방 예약은 ")
    class SaveReservation {

        @Test
        @DisplayName("성공시 예약을 확인할 수 있는 정보를 반환한다.")
        @WithMockAccountContext
        void reserve_one_room_of_one_accommodation_success() throws Exception {
            // given
            ReservationRequest request = ReservationRequest.builder()
                .roomList(List.of(roomRequest))
                .agreement(true)
                .isFromCart(false)
                .build();
            given(reservationService.reserveRoom(any(), any())).willReturn(reservationResponse);

            // when
            ResultActions response = mvc.perform(post("/v1/reservations")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(request)));

            // then
            response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.data.roomList[0].type", is(reservationResponse.getRoomList().get(0).getType())));
        }

        @Test
        @DisplayName("방에 대한 정보가 없을 때는 예약을 할 수 없다.")
        @WithMockAccountContext
        void reserve_no_room_data_fail() throws Exception {
            // given
            ReservationRequest request = ReservationRequest.builder()
                .agreement(true)
                .isFromCart(false)
                .build();
            given(reservationService.reserveRoom(any(), any())).willReturn(reservationResponse);

            // when
            ResultActions response = mvc.perform(post("/v1/reservations")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(request)));

            // then
            response.andExpect(status().isBadRequest())
                .andDo(print());
        }

    }

}
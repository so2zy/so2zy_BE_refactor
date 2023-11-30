package com.aroom.domain.roomCart.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.dto.response.CartAccommodationResponse;
import com.aroom.domain.room.dto.response.CartRoomResponse;
import com.aroom.domain.roomCart.dto.request.RoomCartRequest;
import com.aroom.domain.roomCart.dto.response.FindCartResponse;
import com.aroom.domain.roomCart.dto.response.RoomCartInfoResponse;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.service.RoomCartService;
import com.aroom.util.ControllerTestWithoutSecurityHelper;
import com.aroom.util.security.WithMockAccountContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = RoomCartRestController.class)
public class RoomCartRestControllerTest extends ControllerTestWithoutSecurityHelper {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomCartService roomCartService;

    @Nested
    @DisplayName("postRoomCart()은 ")
    class Context_postRoomCart {

        @Test
        @DisplayName("성공적으로 장바구니에 등록했습니다.")
        @WithMockAccountContext
        void _willSuccess() throws Exception {
            // given
            RoomCartRequest roomCartRequest = RoomCartRequest.builder()
                .startDate(LocalDate.of(2023, 11, 27)).endDate(LocalDate.of(2023, 11, 28)).personnel(3).build();
            List<RoomCartInfoResponse> roomCartInfoResponseList = Arrays.asList(
                RoomCartInfoResponse.builder().room_id(1L).cart_id(1L).build(),
                RoomCartInfoResponse.builder().room_id(1L).cart_id(1L).build()
            );
            RoomCartResponse roomCartResponse = RoomCartResponse.builder()
                .roomCartList(roomCartInfoResponseList).build();
            given(roomCartService.postRoomCart(any(Long.TYPE), any(Long.TYPE),
                any(RoomCartRequest.class))).willReturn(
                roomCartResponse);

            // when, then
            mockMvc.perform(post("/v2/carts/{room_id}", 1L).with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roomCartRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.roomCartList").exists()).andDo(print());
        }
    }

    @Nested
    @DisplayName("장바구니 조회는 ")
    class FindCart {

        @Test
        @DisplayName("성공시 장바구니에 담겨있는 숙소 정보를 반환한다.")
        @WithMockAccountContext
        void find_tester_cart_info_success() throws Exception {
            // given
            CartRoomResponse cartRoomResponse = CartRoomResponse.builder()
                .roomId(1L)
                .type("p1")
                .checkIn(LocalTime.of(10, 00, 00))
                .checkOut(LocalTime.of(11, 00, 00))
                .capacity(2)
                .maxCapacity(4)
                .price(77000)
                .startDate(LocalDate.of(2023, 12, 10))
                .endDate(LocalDate.of(2023, 12, 11))
                .roomImageUrl("asdagnasod.com")
                .build();
            CartAccommodationResponse cartAccommodationResponse = CartAccommodationResponse.builder()
                .accommodationId(1L)
                .accommodationName("남해 글리드 풀빌라")
                .address("경상북도 남해군 남면")
                .roomList(List.of(cartRoomResponse))
                .build();

            FindCartResponse findCartResponse = FindCartResponse.builder()
                .accommodationList(List.of(cartAccommodationResponse))
                .build();
            given(roomCartService.getCartList(any())).willReturn(findCartResponse);

            // when
            ResultActions response = mockMvc.perform(get("/v2/carts")
                .with(csrf()));

            // then
            response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.accommodationList[0].accommodationName",
                    is(cartAccommodationResponse.getAccommodationName())));
        }

    }
}

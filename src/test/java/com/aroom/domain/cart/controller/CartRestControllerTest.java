package com.aroom.domain.cart.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.accommodation.dto.response.CartAccommodationResponse;
import com.aroom.domain.cart.dto.response.FindCartResponse;
import com.aroom.domain.cart.service.CartService;
import com.aroom.domain.room.dto.response.CartRoomResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CartRestController.class)
class CartRestControllerTest {

    @MockBean
    private CartService cartService;

    @Autowired
    private MockMvc mvc;

    @Nested
    @DisplayName("장바구니 조회는 ")
    @WithMockUser("test@naver.com")
    class FindCart{
        @Test
        @DisplayName("성공시 장바구니에 담겨있는 숙소 정보를 반환한다.")
        void find_tester_cart_info_success() throws Exception{
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
            given(cartService.getCartList(any())).willReturn(findCartResponse);

            // when
            ResultActions response = mvc.perform(get("/v1/carts")
                .with(csrf()));

            // then
            response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.accommodationList.accommodationName",
                    is(cartAccommodationResponse.getAccommodationName())));
        }

    }


}
package com.aroom.domain.roomCart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.domain.roomCart.dto.response.RoomCartInfoResponse;
import com.aroom.domain.roomCart.dto.response.RoomCartResponse;
import com.aroom.domain.roomCart.service.RoomCartService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RoomCartRestController.class)
public class RoomCartRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomCartService roomCartService;

    @Nested
    @DisplayName("postRoomCart()은 ")
    class Context_postRoomCart {

        @Test
        @DisplayName("성공적으로 장바구니에 등록했습니다.")
        void _willSuccess() throws Exception {
            // given
            List<RoomCartInfoResponse> roomCartInfoResponseList = Arrays.asList(
                RoomCartInfoResponse.builder().room_id(1L).cart_id(1L).build(),
                RoomCartInfoResponse.builder().room_id(1L).cart_id(1L).build()
            );
            RoomCartResponse roomCartResponse = RoomCartResponse.builder()
                .roomCartList(roomCartInfoResponseList).build();
            given(roomCartService.postRoomCart(any(Long.TYPE), any(Long.TYPE))).willReturn(
                roomCartResponse);

            // when, then
            mockMvc.perform(post("/v1/carts/{member_id}/{room_id}", 1L, 1L).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.roomCartList").exists()).andDo(print());
            verify(roomCartService, times(1)).postRoomCart(any(Long.TYPE), any(Long.TYPE));
        }
    }
}

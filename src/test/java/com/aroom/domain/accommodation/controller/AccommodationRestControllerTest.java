package com.aroom.domain.accommodation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.aroom.domain.accommodation.dto.AccommodationListResponse;
import com.aroom.domain.accommodation.dto.SearchCondition;
import com.aroom.domain.accommodation.service.AccommodationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AccommodationRestController.class)
class AccommodationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccommodationService accommodationService;

    @Test
    void testFindAllAccommodationWithSearchCondition() throws Exception {
        // Mock your service response
        SearchCondition mockSearchCondition = SearchCondition.builder()
            .orderCondition(null)
            .orderBy(null)
            .checkOut(null)
            .name(null)
            .checkIn(null)
            .lowestPrice(null)
            .highestPrice(null)
            .addressCode(null)
            .likeCount(null)
            .phoneNumber(null)
            .build();
        PageRequest mockPageRequest = PageRequest.of(0, 10);
        Sort mockSortCondition = Sort.by(Direction.ASC,
            AccommodationRestController.NO_ORDER_CONDITION);

        List<AccommodationListResponse> accommodationListResponse = List.of(AccommodationListResponse.builder()
            .id(1L)
            .latitude(1)
            .accommodationImageLists(null)
            .phoneNumber("02-000-000")
            .longitude(1)
            .name("야놀자호텔")
            .addressCode("서울시 강남구 청담동 1111")
            .build());
        when(accommodationService.getAccommodationListBySearchCondition(mockSearchCondition, mockPageRequest, mockSortCondition))
            .thenReturn(accommodationListResponse);

        // Call the actual method in the controller
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/accommodations")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockSearchCondition)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("숙소 정보를 성공적으로 조회했습니다."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());


    }
}

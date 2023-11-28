package com.aroom.util;

import com.aroom.domain.accommodation.controller.AccommodationRestController;
import com.aroom.domain.accommodation.service.AccommodationService;
import com.aroom.domain.member.controller.MemberController;
import com.aroom.domain.member.service.MemberQueryService;
import com.aroom.domain.member.service.MemberRegisterService;
import com.aroom.domain.reservation.service.ReservationService;
import com.aroom.global.jwt.controller.JwtRefreshRestController;
import com.aroom.global.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {MemberController.class, JwtRefreshRestController.class,
        AccommodationRestController.class},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)},
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ControllerTestWithoutSecurityHelper {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberRegisterService memberRegisterService;

    @MockBean
    protected MemberQueryService memberQueryService;

    @MockBean
    protected JwtService jwtService;

    @MockBean
    protected ReservationService reservationService;

    @MockBean
    protected AccommodationService accommodationService;
}

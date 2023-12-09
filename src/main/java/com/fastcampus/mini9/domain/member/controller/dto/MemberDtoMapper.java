package com.fastcampus.mini9.domain.member.controller.dto;

import com.fastcampus.mini9.domain.member.controller.dto.request.SignupRequestDto;
import com.fastcampus.mini9.domain.member.controller.dto.response.MemberInfoResponseDto;
import com.fastcampus.mini9.domain.member.controller.dto.response.MemberSaveResponseDto;
import com.fastcampus.mini9.domain.member.service.dto.request.MemberSaveDto;
import com.fastcampus.mini9.domain.member.service.dto.response.MemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 사용방법
 * <p>
 * / @Mapping(source  = "numberOfSeats", target = "seatCount")
 * CarDto carToCarDto(Car car);
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberDtoMapper {

    MemberSaveDto signupTomMemberSave(SignupRequestDto dto);

    MemberSaveResponseDto memberToMemberSaveResponse(MemberDto dto);

    MemberInfoResponseDto memberToMemberInfoResponse(MemberDto dto);
}

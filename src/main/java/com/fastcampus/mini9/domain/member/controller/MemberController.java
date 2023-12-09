package com.fastcampus.mini9.domain.member.controller;

import com.fastcampus.mini9.common.response.BaseResponseBody;
import com.fastcampus.mini9.common.response.DataResponseBody;
import com.fastcampus.mini9.common.response.ErrorResponseBody;
import com.fastcampus.mini9.config.security.token.UserPrincipal;
import com.fastcampus.mini9.domain.member.controller.dto.MemberDtoMapper;
import com.fastcampus.mini9.domain.member.controller.dto.request.LoginRequestDto;
import com.fastcampus.mini9.domain.member.controller.dto.request.SignupRequestDto;
import com.fastcampus.mini9.domain.member.controller.dto.response.LoginResponseDto;
import com.fastcampus.mini9.domain.member.controller.dto.response.MemberInfoResponseDto;
import com.fastcampus.mini9.domain.member.controller.dto.response.MemberSaveResponseDto;
import com.fastcampus.mini9.domain.member.service.MemberService;
import com.fastcampus.mini9.domain.member.service.dto.response.MemberDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberDtoMapper mapper;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
        @ApiResponse(
            description = "정상적으로 호출했을 때",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "잘못된 형식으로 요청했을 때",
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
        ),
        @ApiResponse(
            description = "서버 에러",
            responseCode = "500",
            content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
        )
    })
    @PostMapping("/sign-up")
    public DataResponseBody<MemberSaveResponseDto> signUp(
        @RequestBody @Valid SignupRequestDto request
    ) {
        MemberDto response = memberService.save(mapper.signupTomMemberSave(request));
        return DataResponseBody.success(mapper.memberToMemberSaveResponse(response));
    }

    @Operation(summary = "로그인")
    @ApiResponses(value = {
        @ApiResponse(
            description = "정상적으로 호출했을 때",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "잘못된 형식으로 요청했을 때",
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
        ),
        @ApiResponse(
            description = "서버 에러",
            responseCode = "500",
            content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
        )
    })
    @PostMapping("/login")
    public DataResponseBody<LoginResponseDto> login(
        @RequestBody LoginRequestDto request
    ) {
        throw new RuntimeException("Login Controller");
    }

    @Operation(summary = "로그아웃")
    @ApiResponses(value = {
        @ApiResponse(
            description = "정상적으로 호출했을 때",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "잘못된 형식으로 요청했을 때",
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
        ),
        @ApiResponse(
            description = "서버 에러",
            responseCode = "500",
            content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
        )
    })
    @PostMapping("/logout")
    public BaseResponseBody logout() {
        throw new RuntimeException("Logout Controller");
    }

    @Operation(summary = "회원 정보")
    @ApiResponses(value = {
        @ApiResponse(
            description = "정상적으로 호출했을 때",
            responseCode = "200",
            useReturnTypeSchema = true
        ),
        @ApiResponse(
            description = "잘못된 형식으로 요청했을 때",
            responseCode = "400",
            content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
        ),
        @ApiResponse(
            description = "서버 에러",
            responseCode = "500",
            content = {@Content(schema = @Schema(implementation = ErrorResponseBody.class))}
        )
    })
    @GetMapping("/member-info")
    public DataResponseBody<MemberInfoResponseDto> memberInfo(
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        MemberDto response = memberService.getProfile(principal.getName());
        return DataResponseBody.success(mapper.memberToMemberInfoResponse(response));
    }
}

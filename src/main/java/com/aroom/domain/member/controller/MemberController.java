package com.aroom.domain.member.controller;

import com.aroom.domain.member.dto.request.MemberRegisterRequest;
import com.aroom.domain.member.service.MemberRegisterService;
import com.aroom.global.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberRegisterService memberRegisterService;

    public MemberController(MemberRegisterService memberRegisterService) {
        this.memberRegisterService = memberRegisterService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody MemberRegisterRequest request) {
        memberRegisterService.register(request);
        return ResponseEntity.ok().body(new ApiResponse<>(LocalDateTime.now(), "성공", null));
    }

    @GetMapping("/email/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@NotNull String email) {
        memberRegisterService.validateEmailDuplicatation(email);
        return ResponseEntity.ok().body(new ApiResponse<>(LocalDateTime.now(), "성공", null));
    }
}

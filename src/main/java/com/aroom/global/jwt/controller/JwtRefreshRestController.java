package com.aroom.global.jwt.controller;

import com.aroom.global.jwt.service.JwtService;
import com.aroom.global.jwt.service.TokenResponse;
import com.aroom.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtRefreshRestController {

	private final JwtService jwtService;

	public JwtRefreshRestController(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@PostMapping("/v1/refresh")
	public ResponseEntity<ApiResponse<TokenResponse>> refreshAccessToken(@Valid @RequestBody RefreshAccessTokenRequest request) {
		return ResponseEntity.ok()
			.body(new ApiResponse<>(LocalDateTime.now(), "AccessToken 재발급 성공",
                jwtService.refreshAccessToken(request)));
	}
}

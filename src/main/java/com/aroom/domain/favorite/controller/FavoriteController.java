package com.aroom.domain.favorite.controller;

import com.aroom.domain.favorite.service.FavoriteCreateRequest;
import com.aroom.domain.favorite.service.FavoriteService;
import com.aroom.global.resolver.Login;
import com.aroom.global.resolver.LoginInfo;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/v1/accommodations/{accommodationId}/favorite")
    public ResponseEntity<ApiResponse<Void>> createFavorite(@PathVariable Long accommodationId,
        @Login LoginInfo loginInfo) {
        favoriteService.createFavorite(new FavoriteCreateRequest(accommodationId, loginInfo.memberId()));

        return ResponseEntity.ok(new ApiResponse<>(LocalDateTime.now(), "성공!", null));
    }

    @DeleteMapping("/v1/accommodations/{accommodationId}/favorite")
    public ResponseEntity<ApiResponse<Void>> deleteFavorite(@PathVariable Long accommodationId,
        @Login LoginInfo loginInfo) {
        favoriteService.deleteFavorite(new FavoriteCreateRequest(accommodationId, loginInfo.memberId()));

        return ResponseEntity.ok(new ApiResponse<>(LocalDateTime.now(), "성공!", null));
    }
}

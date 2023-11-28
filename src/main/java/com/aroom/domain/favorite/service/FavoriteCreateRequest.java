package com.aroom.domain.favorite.service;

public record FavoriteCreateRequest(
    Long accommodationId,
    Long memberId
) {

}

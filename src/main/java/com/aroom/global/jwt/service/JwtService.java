package com.aroom.global.jwt.service;

import com.aroom.global.jwt.JwtPayload;
import com.aroom.global.jwt.JwtUtils;
import com.aroom.global.jwt.controller.RefreshAccessTokenRequest;
import com.aroom.global.jwt.exception.BadTokenException;
import com.aroom.global.jwt.model.JwtEntity;
import com.aroom.global.jwt.model.TokenType;
import com.aroom.global.jwt.repository.JwtRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JwtService {

    private final JwtRepository jwtRepository;
    private final JwtUtils jwtUtils;

    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtService(JwtRepository jwtRepository, JwtUtils jwtUtils,
        @Value("${service.jwt.access-expiration}") long accessExpiration,
        @Value("${service.jwt.refresh-expiration}") long refreshExpiration) {
        this.jwtRepository = jwtRepository;
        this.jwtUtils = jwtUtils;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public TokenResponse createTokenPair(JwtPayload jwtPayload) {
        String accessToken = jwtUtils.createToken(jwtPayload, accessExpiration);
        String refreshToken = jwtUtils.createToken(jwtPayload, refreshExpiration);

        jwtRepository.save(
            new JwtEntity(
                createSaveKey(TokenType.REFRESH, jwtPayload.email()),
                createSaveValue(accessToken, refreshToken),
                refreshExpiration));

        return new TokenResponse(accessToken, refreshToken);
    }

    public JwtPayload verifyToken(String token) {
        return jwtUtils.verifyToken(token);
    }

    public TokenResponse refreshAccessToken(RefreshAccessTokenRequest request) {
        JwtPayload jwtPayload = verifyToken(request.refreshToken());

        String savedTokenInfo = jwtRepository
            .getValueByKey(createSaveKey(TokenType.REFRESH, jwtPayload.email()));

        if (!isAcceptable(savedTokenInfo,
            createSaveValue(request.accessToken(), request.refreshToken()))) {
            throw new BadTokenException("적절치 않은 RefreshToken 입니다.");
        }

        String refreshedAccessToken = jwtUtils.createToken(jwtPayload, accessExpiration);

        jwtRepository.save(
            new JwtEntity(
                createSaveKey(TokenType.REFRESH, jwtPayload.email()),
                createSaveValue(refreshedAccessToken, request.refreshToken()),
                refreshExpiration));

        return new TokenResponse(refreshedAccessToken, request.refreshToken());
    }

    private boolean isAcceptable(String one, String other) {
        return one.equals(other);
    }

    private String createSaveKey(TokenType tokenType, String memberId) {
        return "%s_%s".formatted(tokenType.name(), memberId);
    }

    private String createSaveValue(String accessToken, String refreshToken) {
        return "%s:%s".formatted(accessToken, refreshToken);
    }
}

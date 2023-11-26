package com.aroom.global.jwt.service;

import com.aroom.global.jwt.JwtUtils;
import com.aroom.global.jwt.controller.RefreshAccessTokenRequest;
import com.aroom.global.jwt.dto.JwtCreateRequest;
import com.aroom.global.jwt.exception.BadTokenException;
import com.aroom.global.jwt.model.JwtEntity;
import com.aroom.global.jwt.model.JwtPayload;
import com.aroom.global.jwt.model.TokenType;
import com.aroom.global.jwt.repository.JwtRepository;
import java.util.Date;
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

    public TokenResponse createTokenPair(JwtCreateRequest request) {
        JwtPayload jwtPayload = new JwtPayload(request.memberId(), request.name());
        String accessToken = jwtUtils.createToken(jwtPayload, request.issuedAt(), accessExpiration);
        String refreshToken = jwtUtils.createToken(jwtPayload, request.issuedAt(), refreshExpiration);

        jwtRepository.save(
            new JwtEntity(
                createSaveKey(TokenType.REFRESH, jwtPayload.id()),
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
            .getValueByKey(createSaveKey(TokenType.REFRESH, jwtPayload.id()));

        if (!isAcceptable(savedTokenInfo,
            createSaveValue(request.accessToken(), request.refreshToken()))) {
            throw new BadTokenException("적절치 않은 RefreshToken 입니다.");
        }

        String refreshedAccessToken = jwtUtils.createToken(jwtPayload, new Date(), accessExpiration);

        jwtRepository.save(
            new JwtEntity(
                createSaveKey(TokenType.REFRESH, jwtPayload.id()),
                createSaveValue(refreshedAccessToken, request.refreshToken()),
                refreshExpiration));

        return new TokenResponse(refreshedAccessToken, request.refreshToken());
    }

    private boolean isAcceptable(String one, String other) {
        return one.equals(other);
    }

    private String createSaveKey(TokenType tokenType, Long memberId) {
        return "%s_%s".formatted(tokenType.name(), memberId);
    }

    private String createSaveValue(String accessToken, String refreshToken) {
        return "%s:%s".formatted(accessToken, refreshToken);
    }
}

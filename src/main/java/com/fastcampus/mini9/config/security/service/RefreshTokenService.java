package com.fastcampus.mini9.config.security.service;

import com.fastcampus.mini9.config.security.exception.RefreshTokenException;
import com.fastcampus.mini9.config.security.token.AuthenticationDetails;
import com.fastcampus.mini9.config.security.token.UserPrincipal;
import com.fastcampus.mini9.domain.member.entity.RefreshToken;
import com.fastcampus.mini9.domain.member.repository.RefreshTokenRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean isValidRefreshToken(String tokenValue, UserPrincipal principal,
                                       AuthenticationDetails details) throws RefreshTokenException {
        RefreshToken findRefreshToken = refreshTokenRepository.findByTokenValue(tokenValue)
            .orElseThrow(() -> new RefreshTokenException("No Such RefreshToken"));
        return findRefreshToken.getUserId().equals(principal.id())
            && findRefreshToken.getClientIp().equals(details.getClientIp())
            && findRefreshToken.getUserAgent().equals(details.getUserAgent());
    }

    @Transactional
    public String updateRefreshToken(UserPrincipal principal, AuthenticationDetails details) {
        Long userId = principal.id();
        String clientIp = details.getClientIp();
        String userAgent = details.getUserAgent();

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByClientIpAndUserAgent(
            clientIp, userAgent);
        String refreshTokenValue = UUID.randomUUID().toString();
        if (refreshToken.isPresent()) {
            refreshToken.get().update(userId, refreshTokenValue);
            refreshTokenRepository.saveAndFlush(refreshToken.get());
        } else {
            refreshTokenRepository.saveAndFlush(
                new RefreshToken(userId, refreshTokenValue, clientIp, userAgent));
        }

        return refreshTokenValue;
    }
}

package com.fastcampus.mini9.config.security.handler;

import com.fastcampus.mini9.common.response.DataResponseBody;
import com.fastcampus.mini9.common.util.cookie.CookieUtil;
import com.fastcampus.mini9.config.security.provider.JwtProvider;
import com.fastcampus.mini9.config.security.service.RefreshTokenService;
import com.fastcampus.mini9.config.security.token.AuthenticationDetails;
import com.fastcampus.mini9.config.security.token.UserPrincipal;
import com.fastcampus.mini9.domain.member.controller.dto.response.LoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public AjaxAuthenticationSuccessHandler(JwtProvider jwtProvider,
                                            RefreshTokenService refreshTokenService) {
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {
        // access-token
        String accessToken = jwtProvider.generateAccessToken(authentication);

        CookieUtil.addCookieWithoutHttp(response, "access-token", accessToken, 60 * 30);

        // refresh-token
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        AuthenticationDetails details = (AuthenticationDetails) authentication.getDetails();
        String refreshTokenValue = refreshTokenService.updateRefreshToken(principal, details);

        CookieUtil.addCookie(response, "refresh-token", refreshTokenValue, 60 * 60 * 24);

        // response
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(),
            DataResponseBody.success(new LoginResponseDto(accessToken)));
    }
}

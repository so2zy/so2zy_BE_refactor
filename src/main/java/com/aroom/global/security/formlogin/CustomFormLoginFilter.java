package com.aroom.global.security.formlogin;

import com.aroom.global.config.CustomHttpHeaders;
import com.aroom.global.jwt.dto.JwtCreateRequest;
import com.aroom.global.jwt.service.JwtService;
import com.aroom.global.jwt.service.TokenResponse;
import com.aroom.global.security.account.AccountContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class CustomFormLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_USERNAME_PARAMETER = "username";
    private static final String DEFAULT_PASSWORD_PARAMETER = "password";
    private static final String DEFAULT_LOGIN_URL = "/v1/login";

    private final JwtService jwtService;

    public CustomFormLoginFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(DEFAULT_LOGIN_URL);
        setAuthenticationManager(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException {

        String username = request.getParameter(DEFAULT_USERNAME_PARAMETER);
        String password = request.getParameter(DEFAULT_PASSWORD_PARAMETER);

        return getAuthenticationManager().authenticate(
            CustomLoginToken.unAuthenticate(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult) {
        AccountContext context = (AccountContext) authResult.getPrincipal();
        TokenResponse tokenResponse = jwtService.createTokenPair(new JwtCreateRequest(context.getMemberId(), context.getUsername(), new Date()));
        response.setHeader(CustomHttpHeaders.ACCESS_TOKEN, tokenResponse.accessToken());
        response.setHeader(CustomHttpHeaders.REFRESH_TOKEN, tokenResponse.refreshToken());
    }
}

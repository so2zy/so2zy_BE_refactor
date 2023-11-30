package com.aroom.global.security.formlogin;

import com.aroom.global.jwt.dto.JwtCreateRequest;
import com.aroom.global.jwt.service.JwtService;
import com.aroom.global.jwt.service.TokenResponse;
import com.aroom.global.security.account.AccountContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class CustomFormLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_USERNAME_PARAMETER = "username";
    private static final String DEFAULT_PASSWORD_PARAMETER = "password";
    private static final String DEFAULT_LOGIN_URL = "/v1/login";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public CustomFormLoginFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(DEFAULT_LOGIN_URL);
        this.jwtService = jwtService;
        setAuthenticationManager(authenticationManager);
        objectMapper = new ObjectMapper();
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
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {
        AccountContext context = (AccountContext) authResult.getPrincipal();
        TokenResponse tokenResponse = jwtService.createTokenPair(new JwtCreateRequest(context.getMemberId(), context.getName(), new Date()));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
    }
}

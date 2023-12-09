package com.fastcampus.mini9.config.security.filter;

import com.fastcampus.mini9.config.security.token.AjaxAuthenticationToken;
import com.fastcampus.mini9.domain.member.controller.dto.request.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AjaxAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws
        AuthenticationException, IOException, ServletException {
        //boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        //if (!isAjax) {
        //    throw new IllegalStateException("Authentication is not supported");
        //}
        LoginRequestDto loginRequestDto =
            objectMapper.readValue(request.getReader(), LoginRequestDto.class);
        if (!StringUtils.hasText(loginRequestDto.email()) ||
            !StringUtils.hasText(loginRequestDto.pwd())) {
            throw new UsernameNotFoundException("Username Or Password is Empty");
        }
        AjaxAuthenticationToken authRequest = AjaxAuthenticationToken
            .unauthenticated(loginRequestDto.email(), loginRequestDto.pwd());
        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, AjaxAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}

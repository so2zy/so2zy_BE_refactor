package com.aroom.global.security.jwt;

import com.aroom.global.config.CustomHttpHeaders;
import com.aroom.global.security.account.AccountContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        if (!hasJwtToken(request)) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader(CustomHttpHeaders.ACCESS_TOKEN);

        SecurityContextHolder.getContext()
            .setAuthentication(authenticationManager.authenticate(
                JwtAuthenticationToken.unAuthorize(AccountContext.withToken(accessToken))));

        chain.doFilter(request, response);
    }

    private boolean hasJwtToken(HttpServletRequest request) {
        return request.getHeader(CustomHttpHeaders.ACCESS_TOKEN) != null;
    }
}

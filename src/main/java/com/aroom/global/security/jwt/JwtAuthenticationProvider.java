package com.aroom.global.security.jwt;

import com.aroom.global.jwt.model.JwtPayload;
import com.aroom.global.jwt.service.JwtService;
import com.aroom.global.security.account.AccountContext;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();
        JwtPayload jwtPayload = jwtService.verifyToken(accessToken);

        return JwtAuthenticationToken.authorize(
            AccountContext.loginedContext(jwtPayload.id(), jwtPayload.name(),
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}

package com.aroom.util.security;

import com.aroom.global.security.account.AccountContext;
import com.aroom.global.security.jwt.JwtAuthenticationToken;
import java.util.Collections;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockAccountContextSecurityContextFactory implements
    WithSecurityContextFactory<WithMockAccountContext> {

    @Override
    public SecurityContext createSecurityContext(WithMockAccountContext annotation) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        final JwtAuthenticationToken authenticationToken = JwtAuthenticationToken.authorize(
                AccountContext.loginedContext(annotation.memberId(), annotation.name(),
                    Collections.emptySet()));

        securityContext.setAuthentication(authenticationToken);

        return securityContext;
    }
}

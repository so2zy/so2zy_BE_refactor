package com.aroom.global.security.jwt;

import com.aroom.global.security.account.AccountContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AccountContext accountContext;
    private final boolean isAuthenticated;

    private JwtAuthenticationToken(AccountContext accountContext, boolean isAuthenticated) {
        super(accountContext.getAuthorities());
        this.accountContext = accountContext;
        this.isAuthenticated = isAuthenticated;
    }

    public static JwtAuthenticationToken unAuthorize(AccountContext accountContext) {
        return new JwtAuthenticationToken(accountContext, false);
    }

    public static JwtAuthenticationToken authorize(AccountContext accountContext) {
        return new JwtAuthenticationToken(accountContext, true);
    }

    @Override
    public Object getPrincipal() {
        return accountContext;
    }

    @Override
    public Object getCredentials() {
        return accountContext.getPassword();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}

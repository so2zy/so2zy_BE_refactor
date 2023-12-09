package com.fastcampus.mini9.config.security.token;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;
    private boolean isRegenerated = false;
    private String newAccessToken;
    private String newRefreshToken;

    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    public JwtAuthenticationToken(Object principal, Object credentials,
                                  Collection<? extends GrantedAuthority> authorities,
                                  String newAccessToken,
                                  String newRefreshToken) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.isRegenerated = true;
        this.newAccessToken = newAccessToken;
        this.newRefreshToken = newRefreshToken;
        super.setAuthenticated(true); // must use super, as we override
    }

    public static JwtAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new JwtAuthenticationToken(principal, credentials);
    }

    public static JwtAuthenticationToken authenticated(Object principal, Object credentials,
                                                       Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, credentials, authorities);
    }

    public static JwtAuthenticationToken authenticated(Object principal, Object credentials,
                                                       Collection<? extends GrantedAuthority> authorities,
                                                       String newAccessToken,
                                                       String newRefreshToken) {
        return new JwtAuthenticationToken(principal, credentials, authorities, newAccessToken,
            newRefreshToken);
    }

    public boolean isRegenerated() {
        return this.isRegenerated;
    }

    public String getNewAccessToken() {
        return newAccessToken;
    }

    public String getNewRefreshToken() {
        return newRefreshToken;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
            "Cannot set this token to trusted"
                + " - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}

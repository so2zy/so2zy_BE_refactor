package com.aroom.global.security.account;

import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class AccountContext implements UserDetails {

    private final Long memberId;
    private final String name;
    private final String username;
    private final String password;
    private final Collection<GrantedAuthority> authorities;

    private AccountContext(Long memberId, String name, String username, String password,
        Collection<GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static AccountContext beforeLoginContext(String username, String password) {
        return new AccountContext(null, null, username, password, Collections.emptySet());
    }

    public static AccountContext withToken(String token) {
        return new AccountContext(null, null, null, token, Collections.emptySet());
    }

    public static AccountContext loginedContext(Long memberId, String name, Collection<GrantedAuthority> authorities) {
        return new AccountContext(memberId, name, null, null, authorities);
    }

    public static AccountContext fullContext(Long memberId, String name, String username, String password,
        Collection<GrantedAuthority> authorities) {
        return new AccountContext(memberId, name, username, password, authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

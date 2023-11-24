package com.aroom.global.security.account;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class AccountContext implements UserDetails {

    private final Long memberId;
    private final String username;
    private final String password;
    private final Collection<GrantedAuthority> authorities;

    public AccountContext(Long memberId, String username, String password,
        Collection<GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
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

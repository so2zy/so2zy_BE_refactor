package com.aroom.global.security.formlogin;

import com.aroom.global.security.account.AccountContext;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomLoginToken extends AbstractAuthenticationToken {

    private final AccountContext accountContext;

    private CustomLoginToken(AccountContext accountContext) {
        super(accountContext.getAuthorities());
        this.accountContext = accountContext;
    }

    public static CustomLoginToken unAuthenticate(String username, String password) {
        return new CustomLoginToken(AccountContext.beforeLoginContext(username, password));
    }

    public static CustomLoginToken authenticate(AccountContext accountContext) {
        return new CustomLoginToken(accountContext);
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
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        CustomLoginToken that = (CustomLoginToken) object;
        return Objects.equals(accountContext, that.accountContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountContext);
    }
}

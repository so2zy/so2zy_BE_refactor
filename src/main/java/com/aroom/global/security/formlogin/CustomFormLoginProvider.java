package com.aroom.global.security.formlogin;

import com.aroom.global.security.account.AccountContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomFormLoginProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public CustomFormLoginProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        AccountContext requestContext = (AccountContext) authentication.getPrincipal();

        AccountContext accountContext = (AccountContext) userDetailsService
            .loadUserByUsername(requestContext.getUsername());

        if (!passwordEncoder.matches(requestContext.getPassword(), accountContext.getPassword())) {
            throw new BadCredentialsException("Login Fail");
        }

        return CustomLoginToken.authenticate(accountContext);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomLoginToken.class.isAssignableFrom(authentication);
    }
}

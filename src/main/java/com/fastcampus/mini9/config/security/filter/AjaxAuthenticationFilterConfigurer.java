package com.fastcampus.mini9.config.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AjaxAuthenticationFilterConfigurer extends
    AbstractAuthenticationFilterConfigurer<HttpSecurity, AjaxAuthenticationFilterConfigurer, AjaxAuthenticationFilter> {

    private AuthenticationManager authenticationManager;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    public AjaxAuthenticationFilterConfigurer(AjaxAuthenticationFilter authenticationFilter,
                                              String defaultLoginProcessingUrl) {
        super(authenticationFilter, defaultLoginProcessingUrl);
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        super.init(http);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AjaxAuthenticationFilter filter = getAuthenticationFilter();
        if (authenticationManager == null) {
            authenticationManager = http.getSharedObject(AuthenticationManager.class);
        }
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationDetailsSource(authenticationDetailsSource);
        SessionAuthenticationStrategy sessionAuthenticationStrategy = http.getSharedObject(
            SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }
        http.setSharedObject(AjaxAuthenticationFilter.class, getAuthenticationFilter());
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public AjaxAuthenticationFilterConfigurer successHandlerAjax(
        AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public AjaxAuthenticationFilterConfigurer failureHandlerAjax(
        AuthenticationFailureHandler authenticationFailureHandler) {
        this.failureHandler = authenticationFailureHandler;
        return this;
    }

    public AjaxAuthenticationFilterConfigurer setAuthenticationManager(
        AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        return this;
    }

    public AjaxAuthenticationFilterConfigurer setAuthenticationDetailsSource(
        AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }
}

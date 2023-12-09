package com.fastcampus.mini9.config.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.fastcampus.mini9.common.util.cookie.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtLogoutHandler implements LogoutHandler {
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		CookieUtil.deleteCookie(request,response,"access-token");
		CookieUtil.deleteCookie(request,response,"refresh-token");
	}
}

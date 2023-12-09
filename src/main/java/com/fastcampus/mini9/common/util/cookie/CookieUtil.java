package com.fastcampus.mini9.common.util.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    public static void addCookie(
        HttpServletResponse response,
        String name,
        String value,
        int maxAge
    ) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("anti-bias.kr");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("Samesite", "Lax");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void addCookieWithoutHttp(
        HttpServletResponse response,
        String name,
        String value,
        int maxAge
    ) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("anti-bias.kr");
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setAttribute("Samesite", "Lax");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
}

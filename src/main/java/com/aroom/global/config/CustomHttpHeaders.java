package com.aroom.global.config;

import org.springframework.http.HttpHeaders;

public class CustomHttpHeaders extends HttpHeaders {

    public static final String ACCESS_TOKEN = "Access-Token";
    public static final String REFRESH_TOKEN = "Refresh-Token";
}

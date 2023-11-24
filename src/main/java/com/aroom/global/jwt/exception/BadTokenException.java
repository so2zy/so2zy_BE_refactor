package com.aroom.global.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class BadTokenException extends AuthenticationException {

    public BadTokenException(String message) {
        super(message);
    }
}

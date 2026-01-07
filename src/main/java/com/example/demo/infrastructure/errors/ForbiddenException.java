package com.example.demo.infrastructure.errors;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

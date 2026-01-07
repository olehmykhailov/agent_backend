package com.example.demo.infrastructure.errors;

import org.springframework.http.HttpStatus;

public class WrongPasswordException extends BaseException {
    public WrongPasswordException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

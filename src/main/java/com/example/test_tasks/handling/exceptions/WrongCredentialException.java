package com.example.test_tasks.handling.exceptions;

import org.springframework.http.HttpStatus;

public class WrongCredentialException extends RuntimeException {
    public WrongCredentialException(String message) {
        super(message);
    }

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
}

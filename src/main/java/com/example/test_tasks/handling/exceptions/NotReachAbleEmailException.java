package com.example.test_tasks.handling.exceptions;

import org.springframework.http.HttpStatus;

public class NotReachAbleEmailException extends RuntimeException {

    public NotReachAbleEmailException(String message) {
        super(message);
    }

    public static final HttpStatus STATUS = HttpStatus.NOT_ACCEPTABLE;
}

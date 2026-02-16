package com.example.test_tasks.handling.exceptions;

import org.springframework.http.HttpStatus;

public class ObjectAlreadyExistsException extends RuntimeException {

    public ObjectAlreadyExistsException(String message) {
        super(message);
    }

    public static final HttpStatus STATUS = HttpStatus.CONFLICT;
}

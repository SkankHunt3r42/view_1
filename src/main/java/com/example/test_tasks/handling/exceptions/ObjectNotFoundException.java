package com.example.test_tasks.handling.exceptions;

import org.springframework.http.HttpStatus;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;


}

package com.example.test_tasks.handling;


import com.example.test_tasks.handling.exceptions.NotReachAbleEmailException;
import com.example.test_tasks.handling.exceptions.ObjectAlreadyExistsException;
import com.example.test_tasks.handling.exceptions.ObjectNotFoundException;
import com.example.test_tasks.handling.exceptions.WrongCredentialException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class ExceptionInterceptor {


    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ProblemDetail objectAlreadyExists(ObjectAlreadyExistsException e, HttpServletRequest r){
        return bodyBuilder(e,r, ObjectAlreadyExistsException.STATUS);
    }
    @ExceptionHandler(ObjectNotFoundException.class)
    public ProblemDetail objectMissing(ObjectNotFoundException e, HttpServletRequest r){
        return bodyBuilder(e,r,ObjectNotFoundException.STATUS);
    }
    @ExceptionHandler(NotReachAbleEmailException.class)
    public ProblemDetail notReachAble(NotReachAbleEmailException e, HttpServletRequest r){

        return bodyBuilder(e,r,NotReachAbleEmailException.STATUS);
    }
    @ExceptionHandler(WrongThreadException.class)
    public ProblemDetail wrongCredentials(WrongCredentialException e, HttpServletRequest r){

        return bodyBuilder(e,r,WrongCredentialException.STATUS);
    }


    protected ProblemDetail bodyBuilder(Exception e, HttpServletRequest r, HttpStatus s){

        ProblemDetail details = ProblemDetail.forStatus(s);

        details.setDetail(e.getMessage());

        details.setInstance(URI.create(r.getRequestURI()));

        details.setProperty("timeStamp", Instant.now());

        return details;
    }
}

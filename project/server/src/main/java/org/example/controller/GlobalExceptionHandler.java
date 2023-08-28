package org.example.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleException(DataAccessException ex) {

        ex.printStackTrace();

        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse("We can't show you the details, but something went wrong in our database. Sorry :("),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                new ErrorResponse("Your input is invalid. Try again."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                new ErrorResponse("Something went wrong on our end. Your request failed. :("),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
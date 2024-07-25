package com.example.deal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<DealException> handleTheException(RuntimeException e, HttpStatus status) {
        return new ResponseEntity<>(
                new DealException(e.getMessage(), status.value()),
                status
        );
    }

    @ExceptionHandler(ConveyorException.class)
    public ResponseEntity<DealException> handleConveyorException(ConveyorException e) {
        return handleTheException(e, HttpStatus.resolve(e.getStatus()));
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<DealException> handleConnectException(ConnectException e) {
        return handleTheException(new RuntimeException("Connection refused."), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<DealException> handleApplicationNotFoundException(ApplicationNotFoundException e) {
        return handleTheException(e, HttpStatus.NOT_FOUND);
    }
}

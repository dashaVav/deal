package com.example.deal.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<DealError> handleTheException(RuntimeException e, HttpStatus status) {
        log.error("Exception: {} handled normally. Message: {}", e.getClass().getName(), e.getMessage());
        return new ResponseEntity<>(
                new DealError(e.getMessage(), status.value()),
                status
        );
    }

    @ExceptionHandler(ConveyorException.class)
    public ResponseEntity<DealError> handleConveyorException(ConveyorException e) {
        return handleTheException(e, HttpStatus.resolve(e.getStatus()));
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<DealError> handleConnectException(ConnectException e) {
        return handleTheException(new RuntimeException("Service Unavailable"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({ApplicationNotFoundException.class, OfferDoesNotExistException.class})
    public ResponseEntity<DealError> handleNotFoundException(RuntimeException e) {
        return handleTheException(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSesCodeException.class)
    public ResponseEntity<DealError> handleBadRequestsException(RuntimeException e) {
        return handleTheException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnresolvedOperationException.class)
    public ResponseEntity<DealError> handleMethodNotAllowedException(UnresolvedOperationException e) {
        return handleTheException(e, HttpStatus.METHOD_NOT_ALLOWED);
    }
}

package com.example.deal.exception;

public class OfferDoesNotExistException extends RuntimeException {
    public OfferDoesNotExistException(String message) {
        super(message);
    }
}

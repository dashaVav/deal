package com.example.deal.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class ConveyorException extends RuntimeException {
    private final String error;
    private final Integer status;

    public ConveyorException(String error, Integer status) {
        super(error);
        this.error = error;
        this.status = status;
    }
}

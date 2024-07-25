package com.example.deal.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DealException {
    private String error;
    private Integer status;
}

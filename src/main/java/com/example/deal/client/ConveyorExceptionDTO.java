package com.example.deal.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConveyorExceptionDTO {
    private String error;
    private Integer status;
}

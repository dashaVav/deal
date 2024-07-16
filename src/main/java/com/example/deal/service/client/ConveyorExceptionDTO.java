package com.example.deal.service.client;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConveyorExceptionDTO {
    private String error;
    private Integer status;
}

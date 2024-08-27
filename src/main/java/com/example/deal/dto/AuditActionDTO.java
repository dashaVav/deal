package com.example.deal.dto;

import com.example.deal.dto.enums.Service;
import com.example.deal.dto.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuditActionDTO {
    private UUID id;
    private Type type;
    private Service service;
    private String message;
}

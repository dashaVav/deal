package com.example.deal.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmailMessage {
    private String address;
    private EmailMessageStatus theme;
    private Long applicationId;
}

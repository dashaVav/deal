package com.example.deal.dto;

import com.example.deal.dto.enums.EmailMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String address;
    private EmailMessageStatus theme;
    private Long applicationId;
}

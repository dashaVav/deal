package com.example.deal.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String address;
    private EmailMessageStatus theme;
    private Long applicationId;
}

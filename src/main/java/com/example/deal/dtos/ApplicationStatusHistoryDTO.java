package com.example.deal.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistoryDTO {
    private ApplicationStatus applicationStatus;
    private LocalDateTime time;
    private StatusHistory changeType;
}
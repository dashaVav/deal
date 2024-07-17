package com.example.deal.model;

import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.model.enums.StatusHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistory {
    private ApplicationStatus applicationStatus;
    private LocalDateTime time;
    private StatusHistory changeType;
}
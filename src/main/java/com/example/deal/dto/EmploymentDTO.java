package com.example.deal.dto;

import com.example.deal.dto.enums.EmploymentPosition;
import com.example.deal.dto.enums.EmploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {
    @NotNull
    private EmploymentStatus employmentStatus;

    @NotBlank
    private String employerINN;

    @NotNull
    private BigDecimal salary;

    @NotNull
    private EmploymentPosition position;

    @NotNull
    private Integer workExperienceTotal;

    @NotNull
    private Integer workExperienceCurrent;
}

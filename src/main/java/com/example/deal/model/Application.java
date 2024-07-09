package com.example.deal.model;

import com.example.deal.dtos.ApplicationStatus;
import com.example.deal.dtos.ApplicationStatusHistoryDTO;
import com.example.deal.dtos.LoanOfferDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "application")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "credit_id")
    private Long creditId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDTO appliedOffer;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    @Column(name = "status_history")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ApplicationStatusHistoryDTO> statusHistory = new ArrayList<>();
}

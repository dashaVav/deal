package com.example.deal.model;

import com.example.deal.dtos.ApplicationStatus;
import com.example.deal.dtos.ApplicationStatusHistoryDTO;
import com.example.deal.dtos.CreditDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "application")
public class Application {
    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long application_id;
    @Column(name = "client")
    private long client_id;

    @Column(name = "credit")
    private long credit_id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    private CreditDTO appliedOffer;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    @Column(name = "status_history")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ApplicationStatusHistoryDTO> statusHistory;

//    @OneToOne
//    private Client client1;
}

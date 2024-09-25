package com.example.deal.model;

import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.model.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "application")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"client", "credit"})
@EqualsAndHashCode(exclude = {"client", "credit"})
public class Application {
    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDTO appliedOffer;

    @Column(name = "loan_offers")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<LoanOfferDTO> loanOffers;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    @Column(name = "status_history")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ApplicationStatusHistory> statusHistory = new ArrayList<>();

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "credit_id")
    private Credit credit;

    public List<ApplicationStatusHistory> getStatusHistory() {
        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }
        return statusHistory;
    }
}

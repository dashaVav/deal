package com.example.deal.service;

import com.example.deal.client.ConveyorClient;
import com.example.deal.dtos.*;
import com.example.deal.exception.ApplicationNotFoundException;
import com.example.deal.mappers.ClientMapper;
import com.example.deal.mappers.CreditMapper;
import com.example.deal.mappers.ScoringDataDTOMapper;
import com.example.deal.model.Application;
import com.example.deal.model.Client;
import com.example.deal.model.Credit;
import com.example.deal.model.CreditStatus;
import com.example.deal.repository.JpaApplicationRepository;
import com.example.deal.repository.JpaClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {
    private final JpaClientRepository clientRepository;
    private final JpaApplicationRepository applicationRepository;
    private final ConveyorClient conveyorClient;

    @Transactional
    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        Client savedClient = clientRepository.save(ClientMapper.INSTANCE.from(loanApplicationRequest));

        Application savedApplication = applicationRepository.save(
                Application.builder()
                        .client(savedClient)
                        .creationDate(LocalDateTime.now())
                        .build()
        );

        return setApplicationId(savedApplication.getApplicationId(), conveyorClient.offers(loanApplicationRequest));
    }

    private List<LoanOfferDTO> setApplicationId(long applicationId, List<LoanOfferDTO> loanOfferDTOS) {
        loanOfferDTOS.forEach(loanOffer -> loanOffer.setApplicationId(applicationId));
        return loanOfferDTOS;
    }

    @Transactional
    public void offer(LoanOfferDTO loanOffer) {
        Application application = applicationRepository.findById(loanOffer.getApplicationId())
                .orElseThrow(() -> new ApplicationNotFoundException("Application id not found."));

        application.setAppliedOffer(loanOffer);
        application.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        application.getStatusHistory().add(
                initApplicationStatusHistoryDTO(application.getApplicationStatus())
        );
        applicationRepository.save(application);
    }

    @Transactional
    public void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application id not found."));

        ScoringDataDTO scoringDataDTO = ScoringDataDTOMapper.INSTANCE.from(
                application.getClient(),
                finishRegistrationRequest,
                application.getAppliedOffer()
        );

        application.setClient(ClientMapper.INSTANCE.from(application.getClient(), finishRegistrationRequest));

        Credit credit = CreditMapper.INSTANCE.from(conveyorClient.calculation(scoringDataDTO));
        credit.setCreditStatus(CreditStatus.CALCULATED);
        application.setCredit(credit);

        application.setApplicationStatus(ApplicationStatus.APPROVED);
        application.getStatusHistory().add(
                initApplicationStatusHistoryDTO(application.getApplicationStatus())
        );
        applicationRepository.save(application);
    }

    private ApplicationStatusHistoryDTO initApplicationStatusHistoryDTO(ApplicationStatus applicationStatus) {
        return ApplicationStatusHistoryDTO
                .builder()
                .applicationStatus(applicationStatus)
                .time(LocalDateTime.now())
                .changeType(StatusHistory.AUTOMATIC)
                .build();
    }
}

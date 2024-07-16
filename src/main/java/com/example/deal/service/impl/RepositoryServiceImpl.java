package com.example.deal.service.impl;

import com.example.deal.dtos.*;
import com.example.deal.exception.ApplicationNotFoundException;
import com.example.deal.mappers.ClientMapper;
import com.example.deal.mappers.CreditMapper;
import com.example.deal.mappers.ScoringDataDTOMapper;
import com.example.deal.model.*;
import com.example.deal.repository.JpaApplicationRepository;
import com.example.deal.repository.JpaClientRepository;
import com.example.deal.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepositoryServiceImpl implements RepositoryService {
    private final JpaClientRepository clientRepository;
    private final JpaApplicationRepository applicationRepository;

    @Override
    public Long createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        Client savedClient = clientRepository.save(ClientMapper.INSTANCE.from(loanApplicationRequest));

        Application savedApplication = applicationRepository.save(
                Application.builder()
                        .client(savedClient)
                        .applicationStatus(ApplicationStatus.PREAPPROVAL)
                        .creationDate(LocalDateTime.now())
                        .statusHistory(List.of(initApplicationStatusHistoryDTO(ApplicationStatus.PREAPPROVAL)))
                        .build()
        );

        return savedApplication.getApplicationId();
    }

    @Override
    public void offer(LoanOfferDTO loanOffer) {
        Application application = getApplicationById(loanOffer.getApplicationId());

        application.setAppliedOffer(loanOffer);
        application.setApplicationStatus(ApplicationStatus.APPROVED);
        application.getStatusHistory().add(
                initApplicationStatusHistoryDTO(application.getApplicationStatus())
        );
        applicationRepository.save(application);
    }

    @Override
    public void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId, CreditDTO creditDTO) {
        Application application = getApplicationById(applicationId);

        application.setClient(ClientMapper.INSTANCE.from(application.getClient(), finishRegistrationRequest));

        Credit credit = CreditMapper.INSTANCE.from(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        application.setCredit(credit);

        application.setApplicationStatus(ApplicationStatus.CC_APPROVED);
        application.getStatusHistory().add(
                initApplicationStatusHistoryDTO(application.getApplicationStatus())
        );
        applicationRepository.save(application);
    }

    @Override
    public ScoringDataDTO getScoringData(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        Application application = getApplicationById(applicationId);

        return ScoringDataDTOMapper.INSTANCE.from(
                application.getClient(),
                finishRegistrationRequest,
                application.getAppliedOffer()
        );
    }

    @Override
    public String getEmailAddressByApplicationId(Long applicationId) {
        Application application = getApplicationById(applicationId);
        return application.getClient().getEmail();
    }

    private ApplicationStatusHistory initApplicationStatusHistoryDTO(ApplicationStatus applicationStatus) {
        return ApplicationStatusHistory
                .builder()
                .applicationStatus(applicationStatus)
                .time(LocalDateTime.now())
                .changeType(StatusHistory.AUTOMATIC)
                .build();
    }

    private Application getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application id not found."));
    }
}

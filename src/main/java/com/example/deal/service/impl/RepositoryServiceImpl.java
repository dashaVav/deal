package com.example.deal.service.impl;

import com.example.deal.dto.*;
import com.example.deal.exception.ApplicationNotFoundException;
import com.example.deal.exception.OfferDoesNotExistException;
import com.example.deal.mapper.MergeClientService;
import com.example.deal.model.Application;
import com.example.deal.model.ApplicationStatusHistory;
import com.example.deal.model.Client;
import com.example.deal.model.Credit;
import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.model.enums.CreditStatus;
import com.example.deal.model.enums.StatusHistory;
import com.example.deal.repository.JpaApplicationRepository;
import com.example.deal.repository.JpaClientRepository;
import com.example.deal.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RepositoryServiceImpl implements RepositoryService {
    private final JpaClientRepository clientRepository;
    private final JpaApplicationRepository applicationRepository;
    private final ConversionService conversionService;
    private final MergeClientService mergeClientService;

    @Override
    public Long createApplicationWithClient(LoanApplicationRequestDTO loanApplicationRequest) {
        Client savedClient = clientRepository.save(
                Objects.requireNonNull(conversionService.convert(loanApplicationRequest, Client.class))
        );
        Application savedApplication = applicationRepository.save(
                new Application()
                        .setClient(savedClient)
                        .setApplicationStatus(ApplicationStatus.PREAPPROVAL)
                        .setCreationDate(LocalDateTime.now())
                        .setStatusHistory(new ArrayList<>(List.of(initApplicationStatusHistoryDTO(ApplicationStatus.PREAPPROVAL))))
        );

        return savedApplication.getApplicationId();
    }


    @Override
    public void validateOffer(LoanOfferDTO loanOffer) {
        Application application = getApplicationById(loanOffer.getApplicationId());
        if (!application.getLoanOffers().contains(loanOffer)) {
            updateApplicationStatusAndHistory(application, ApplicationStatus.CLIENT_DENIED);
            applicationRepository.save(application);
            throw new OfferDoesNotExistException("Selected offer does not exist.");
        }
    }

    private void updateApplicationStatusAndHistory(Application application, ApplicationStatus applicationStatus) {
        application.setApplicationStatus(applicationStatus);
        application.getStatusHistory().add(
                initApplicationStatusHistoryDTO(application.getApplicationStatus())
        );
    }

    @Override
    public void offer(LoanOfferDTO loanOffer) {
        Application application = getApplicationById(loanOffer.getApplicationId());
        application.setAppliedOffer(loanOffer);
        updateApplicationStatusAndHistory(application, ApplicationStatus.APPROVED);
        applicationRepository.save(application);
    }

    @Override
    public void saveClientAdditionalInfo(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        Application application = getApplicationById(applicationId);
        application.setClient(mergeClientService.mergeClient(application.getClient(),
                conversionService.convert(finishRegistrationRequest, Client.class)));
        applicationRepository.save(application);
    }

    @Override
    public void calculate(Long applicationId, CreditDTO creditDTO) {
        Application application = getApplicationById(applicationId);

        Credit credit = conversionService.convert(creditDTO, Credit.class);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        application.setCredit(credit);

        updateApplicationStatusAndHistory(application, ApplicationStatus.CC_APPROVED);
        applicationRepository.save(application);
    }

    @Override
    public ScoringDataDTO getScoringData(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        Application application = getApplicationById(applicationId);
        ScoringDataDTO scoringDataDTO = conversionService.convert(application.getClient(), ScoringDataDTO.class);
        LoanOfferDTO appliedOffer = application.getAppliedOffer();
        scoringDataDTO.setAmount(appliedOffer.getRequestedAmount());
        scoringDataDTO.setTerm(appliedOffer.getTerm());
        scoringDataDTO.setIsInsuranceEnabled(appliedOffer.getIsInsuranceEnabled());
        scoringDataDTO.setIsSalaryClient(appliedOffer.getIsSalaryClient());
        return scoringDataDTO;
    }

    @Override
    public String getEmailAddressByApplicationId(Long applicationId) {
        Application application = getApplicationById(applicationId);
        return application.getClient().getEmail();
    }

    private ApplicationStatusHistory initApplicationStatusHistoryDTO(ApplicationStatus applicationStatus) {
        return new ApplicationStatusHistory()
                .setApplicationStatus(applicationStatus)
                .setTime(LocalDateTime.now())
                .setChangeType(StatusHistory.AUTOMATIC);
    }

    @Override
    public Application getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application id not found."));
    }

    @Override
    public void setApplicationStatus(Long applicationId, ApplicationStatus applicationStatus) {
        Application application = getApplicationById(applicationId);
        updateApplicationStatusAndHistory(application, applicationStatus);
        applicationRepository.save(application);
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public void setSesCode(Long applicationId, String seCode) {
        Application application = getApplicationById(applicationId);
        application.setSesCode(seCode);
        applicationRepository.save(application);
    }

    @Override
    public String getSesCode(Long applicationId) {
        Application application = getApplicationById(applicationId);
        return application.getSesCode();
    }

    @Override
    public void saveLoanOffers(Long applicationId, List<LoanOfferDTO> loanOffers) {
        Application application = getApplicationById(applicationId);
        application.setLoanOffers(loanOffers);
        applicationRepository.save(application);
    }

    @Override
    public ApplicationStatus getApplicationStatus(Long applicationId) {
        return getApplicationById(applicationId).getApplicationStatus();
    }

    @Override
    public void setCreditStatus(Long applicationId, CreditStatus creditStatus) {
        Application application = getApplicationById(applicationId);
        Credit credit = application.getCredit();
        credit.setCreditStatus(creditStatus);
        applicationRepository.save(application);
    }

    @Override
    public void setSignDate(Long applicationId) {
        Application application = getApplicationById(applicationId);
        application.setSignDate(LocalDateTime.now());
        applicationRepository.save(application);
    }
}

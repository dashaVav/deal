package com.example.deal.service;

import com.example.deal.dto.*;
import com.example.deal.model.Application;
import com.example.deal.model.enums.ApplicationStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RepositoryService {
    @Transactional
    Long createApplicationWithClient(LoanApplicationRequestDTO loanApplicationRequest);

    void validationOfOffer(LoanOfferDTO loanOffer);

    @Transactional
    void offer(LoanOfferDTO loanOffer);

    @Transactional
    void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId, CreditDTO creditDTO);

    ScoringDataDTO getScoringData(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId);

    String getEmailAddressByApplicationId(Long applicationId);

    Application getApplicationById(Long applicationId);

    @Transactional
    void updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus);

    List<Application> getAllApplications();

    @Transactional
    void setSesCode(Long applicationId, String seCode);

    String getSesCode(Long applicationId);

    @Transactional
    void setSignDate(Long applicationId);

    @Transactional
    void saveLoanOffers(Long applicationId, List<LoanOfferDTO> loanOffers);

    ApplicationStatus getApplicationStatus(Long applicationId);
}

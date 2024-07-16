package com.example.deal.service;

import com.example.deal.dtos.*;
import org.springframework.transaction.annotation.Transactional;

public interface RepositoryService {
    @Transactional
    Long createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);

    @Transactional
    void offer(LoanOfferDTO loanOffer);

    @Transactional
    void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId, CreditDTO creditDTO);

    ScoringDataDTO getScoringData(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId);

    String getEmailAddressByApplicationId(Long applicationId);
}

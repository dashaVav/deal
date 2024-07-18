package com.example.deal.service;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DealService {
    @Transactional
    List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);

    void offer(LoanOfferDTO loanOffer);

    void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId);
}
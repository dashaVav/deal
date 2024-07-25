package com.example.deal.service;

import com.example.deal.dtos.FinishRegistrationRequestDTO;
import com.example.deal.dtos.LoanApplicationRequestDTO;
import com.example.deal.dtos.LoanOfferDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DealService {
    @Transactional
    List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest);

    @Transactional
    void offer(LoanOfferDTO loanOffer);

    @Transactional
    void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId);
}

package com.example.deal.controller.impl;


import com.example.deal.controller.DealController;
import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealControllerImpl implements DealController {
    private final DealService dealService;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> application(LoanApplicationRequestDTO loanApplicationRequest) {
        return ResponseEntity.ok(dealService.createLoanOffers(loanApplicationRequest));
    }

    @Override
    public ResponseEntity<Void> offer(LoanOfferDTO loanOffer) {
        dealService.offer(loanOffer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> calculation(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        dealService.calculate(finishRegistrationRequest, applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

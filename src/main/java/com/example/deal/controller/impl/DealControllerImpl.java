package com.example.deal.controller.impl;


import com.example.deal.controller.DealController;
import com.example.deal.dtos.FinishRegistrationRequestDTO;
import com.example.deal.dtos.LoanApplicationRequestDTO;
import com.example.deal.dtos.LoanOfferDTO;
import com.example.deal.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealControllerImpl implements DealController {
    private final ClientService clientService;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> application(LoanApplicationRequestDTO loanApplicationRequest) {
        return ResponseEntity.ok(clientService.createLoanOffers(loanApplicationRequest));
    }

    @Override
    public ResponseEntity<Void> offer(LoanOfferDTO loanOffer) {
        clientService.offer(loanOffer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> calculation(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        clientService.calculate(finishRegistrationRequest, applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

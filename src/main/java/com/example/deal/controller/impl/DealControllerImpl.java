package com.example.deal.controller.impl;


import com.example.deal.controller.DealController;
import com.example.deal.dtos.FinishRegistrationRequestDTO;
import com.example.deal.dtos.LoanApplicationRequestDTO;
import com.example.deal.dtos.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealControllerImpl implements DealController {

    @Override
    public ResponseEntity<List<LoanOfferDTO>> application(LoanApplicationRequestDTO loanApplicationRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Void> offer(LoanOfferDTO loanOffer) {
        return null;
    }

    @Override
    public ResponseEntity<Void> calculation(FinishRegistrationRequestDTO finishRegistrationRequest, String applicationId) {
        return null;
    }
}

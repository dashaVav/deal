package com.example.deal.controller.impl;


import com.example.deal.annotation.AuditAction;
import com.example.deal.controller.DealController;
import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.service.DealService;
import com.example.deal.utils.LoggerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DealControllerImpl implements DealController {
    private final DealService dealService;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> application(LoanApplicationRequestDTO loanApplicationRequest) {
        log.info("/deal/application requested. Body: {}...", LoggerUtils.cut(loanApplicationRequest, 100));
        return ResponseEntity.ok(dealService.createLoanOffers(loanApplicationRequest));
    }

    @AuditAction(message = "Application is saved with APPROVED status.")
    @Override
    public ResponseEntity<Void> offer(LoanOfferDTO loanOffer) {
        log.info("/deal/offer requested with id - {}", loanOffer.getApplicationId());
        dealService.offer(loanOffer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> calculation(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        log.info("/deal/calculate/{applicationId} requested with id - {}", applicationId);
        dealService.calculate(finishRegistrationRequest, applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.example.deal.controller;


import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/deal")
public interface DealController {
    @PostMapping(path = "/application")
    ResponseEntity<List<LoanOfferDTO>> application(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequest);

    @PutMapping(path = "/offer")
    ResponseEntity<Void> offer(@Valid @RequestBody LoanOfferDTO loanOffer);

    @PutMapping(path = "/calculate/{applicationId}")
    ResponseEntity<Void> calculation(@Valid @RequestBody FinishRegistrationRequestDTO finishRegistrationRequest, @PathVariable Long applicationId);
}

package com.example.deal.controller;


import com.example.deal.dtos.FinishRegistrationRequestDTO;
import com.example.deal.dtos.LoanApplicationRequestDTO;
import com.example.deal.dtos.LoanOfferDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/deal")
public interface DealController {
    @PostMapping(path = "/application")
    ResponseEntity<List<LoanOfferDTO>> application(@RequestBody LoanApplicationRequestDTO loanApplicationRequest);

    @PutMapping(path = "/offer")
    ResponseEntity<Void> offer(@RequestBody LoanOfferDTO loanOffer);

    @PutMapping(path = "/calculate/{applicationId}")
    ResponseEntity<Void> calculation(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequest, @PathVariable Long applicationId);
}

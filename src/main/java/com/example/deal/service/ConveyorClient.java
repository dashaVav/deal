package com.example.deal.service;

import com.example.deal.dto.CreditDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "conveyor-client", url = "${feign-client.conveyor-client.base-url}")
public interface ConveyorClient {
    @PostMapping(
            value = "${feign-client.conveyor-client.offers-path}",
            consumes = "application/json"
    )
    List<LoanOfferDTO> offers(LoanApplicationRequestDTO loanApplicationRequest);

    @PostMapping(
            value = "${feign-client.conveyor-client.calculation-path}",
            consumes = "application/json"
    )
    CreditDTO calculation(ScoringDataDTO scoringData);
}

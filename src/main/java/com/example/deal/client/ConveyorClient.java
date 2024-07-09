package com.example.deal.client;

import com.example.deal.dtos.CreditDTO;
import com.example.deal.dtos.LoanApplicationRequestDTO;
import com.example.deal.dtos.LoanOfferDTO;
import com.example.deal.dtos.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "conveyorClient", url = "http://localhost:8091")
public interface ConveyorClient {
    @RequestMapping(method = RequestMethod.POST, value = "/conveyor/offers", consumes = "application/json")
    List<LoanOfferDTO> offers(LoanApplicationRequestDTO loanApplicationRequest);

    @RequestMapping(method = RequestMethod.POST, value = "/conveyor/calculation", consumes = "application/json")
    CreditDTO calculation(ScoringDataDTO scoringData);
}

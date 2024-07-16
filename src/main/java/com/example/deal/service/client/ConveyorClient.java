package com.example.deal.service.client;

import com.example.deal.dtos.CreditDTO;
import com.example.deal.dtos.LoanApplicationRequestDTO;
import com.example.deal.dtos.LoanOfferDTO;
import com.example.deal.dtos.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "conveyor-client", url = "${feign-client.conveyor-client.base-url}")
public interface ConveyorClient {
    @RequestMapping(method = RequestMethod.POST,
            value = "${feign-client.conveyor-client.offers-path}",
            consumes = "application/json"
    )
    List<LoanOfferDTO> offers(LoanApplicationRequestDTO loanApplicationRequest);

    @RequestMapping(method = RequestMethod.POST,
            value = "${feign-client.conveyor-client.calculation-path}",
            consumes = "application/json"
    )
    CreditDTO calculation(ScoringDataDTO scoringData);
}

package com.example.deal.service;

import com.example.deal.client.ConveyorClient;
import com.example.deal.dtos.*;
import com.example.deal.mappers.ClientMapper;
import com.example.deal.mappers.CreditMapper;
import com.example.deal.mappers.ScoringDataDTOMapper;
import com.example.deal.model.Application;
import com.example.deal.model.Client;
import com.example.deal.model.Credit;
import com.example.deal.model.CreditStatus;
import com.example.deal.repository.JpaApplicationRepository;
import com.example.deal.repository.JpaClientRepository;
import com.example.deal.repository.JpaCreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final JpaClientRepository clientRepository;
    private final JpaApplicationRepository applicationRepository;
    private final JpaCreditRepository creditRepository;
    private final ConveyorClient conveyorClient;

    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO client) {
        Client savedClient = clientRepository.save(ClientMapper.INSTANCE.from(client));

        Application application = new Application();
        application.setClientId(savedClient.getClientId());
        application.setCreationDate(LocalDateTime.now());
        Application savedApplication = applicationRepository.save(application);

        return setApplicationId(savedApplication.getApplicationId(), conveyorClient.offers(client));
    }

    private List<LoanOfferDTO> setApplicationId(long applicationId, List<LoanOfferDTO> loanOfferDTOS) {
        loanOfferDTOS.forEach(loanOffer -> loanOffer.setApplicationId(applicationId));
        return loanOfferDTOS;
    }

    public void offer(LoanOfferDTO loanOffer) {
        Application application = applicationRepository.getReferenceById(loanOffer.getApplicationId());
        application.setAppliedOffer(loanOffer);
        application.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        application.getStatusHistory().add(new ApplicationStatusHistoryDTO(application.getApplicationStatus(), LocalDateTime.now(), StatusHistory.AUTOMATIC));

        applicationRepository.save(application);
    }

    public void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        Application application = applicationRepository.getReferenceById(applicationId);
        Client client = clientRepository.getReferenceById(application.getClientId());

        ScoringDataDTO scoringDataDTO = ScoringDataDTOMapper.INSTANCE.from(client, finishRegistrationRequest, application.getAppliedOffer());
        System.out.println(scoringDataDTO);
        client = ClientMapper.INSTANCE.from(client, finishRegistrationRequest);
        clientRepository.save(client);

        Credit credit = CreditMapper.INSTANCE.from(conveyorClient.calculation(scoringDataDTO));
        credit.setCreditStatus(CreditStatus.CALCULATED);
        Credit savedClient = creditRepository.save(credit);

        application.setCreditId(savedClient.getCreditId());
        application.setApplicationStatus(ApplicationStatus.APPROVED);
        application.getStatusHistory().add(new ApplicationStatusHistoryDTO(application.getApplicationStatus(), LocalDateTime.now(), StatusHistory.AUTOMATIC));

        applicationRepository.save(application);
    }
}

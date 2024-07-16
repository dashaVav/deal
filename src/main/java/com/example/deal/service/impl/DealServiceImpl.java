package com.example.deal.service.impl;

import com.example.deal.dtos.*;
import com.example.deal.service.DealService;
import com.example.deal.service.RepositoryService;
import com.example.deal.service.client.ConveyorClient;
import com.example.deal.service.kafka.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final RepositoryService repositoryService;
    private final ConveyorClient conveyorClient;
    private final NotificationProducer notificationProducer;

    @Override
    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        Long applicationId = repositoryService.createLoanOffers(loanApplicationRequest);
        return setApplicationId(applicationId, conveyorClient.offers(loanApplicationRequest));
    }

    private List<LoanOfferDTO> setApplicationId(long applicationId, List<LoanOfferDTO> loanOfferDTOS) {
        loanOfferDTOS.forEach(loanOffer -> loanOffer.setApplicationId(applicationId));
        return loanOfferDTOS;
    }

    @Override
    public void offer(LoanOfferDTO loanOffer) {
        repositoryService.offer(loanOffer);
        notificationProducer.produceFinishRegistration(
                createEmailMessage(loanOffer.getApplicationId(), EmailMessageStatus.FINISH_REGISTRATION)
        );
    }

    @Override
    public void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        ScoringDataDTO scoringData = repositoryService.getScoringData(finishRegistrationRequest, applicationId);
        CreditDTO credit = conveyorClient.calculation(scoringData);
        repositoryService.calculate(finishRegistrationRequest, applicationId, credit);

        notificationProducer.produceCreateDocuments(
                createEmailMessage(applicationId, EmailMessageStatus.CREATE_DOCUMENTS)
        );
    }

    private EmailMessage createEmailMessage(Long applicationId, EmailMessageStatus emailMessageStatus) {
        EmailMessage emailMessage = new EmailMessage();
        return emailMessage.setAddress(repositoryService.getEmailAddressByApplicationId(applicationId))
                .setApplicationId(applicationId)
                .setTheme(emailMessageStatus);
    }
}

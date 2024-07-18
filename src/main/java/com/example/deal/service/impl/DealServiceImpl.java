package com.example.deal.service.impl;

import com.example.deal.dto.*;
import com.example.deal.dto.enums.EmailMessageStatus;
import com.example.deal.exception.ConveyorException;
import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.service.ConveyorClient;
import com.example.deal.service.DealService;
import com.example.deal.service.NotificationProducer;
import com.example.deal.service.RepositoryService;
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
        List<LoanOfferDTO> loanOffers = conveyorClient.offers(loanApplicationRequest);
        repositoryService.saveLoanOffers(applicationId, loanOffers);
        return setApplicationId(applicationId, loanOffers);
    }

    private List<LoanOfferDTO> setApplicationId(long applicationId, List<LoanOfferDTO> loanOfferDTOS) {
        loanOfferDTOS.forEach(loanOffer -> loanOffer.setApplicationId(applicationId));
        return loanOfferDTOS;
    }

    @Override
    public void offer(LoanOfferDTO loanOffer) {
        repositoryService.offer(loanOffer);
        notificationProducer.produceFinishRegistration(
                new EmailMessage(
                        repositoryService.getEmailAddressByApplicationId(loanOffer.getApplicationId()),
                        EmailMessageStatus.FINISH_REGISTRATION,
                        loanOffer.getApplicationId()
                )
        );
    }

    @Override
    public void calculate(FinishRegistrationRequestDTO finishRegistrationRequest, Long applicationId) {
        ScoringDataDTO scoringData = repositoryService.getScoringData(finishRegistrationRequest, applicationId);

        CreditDTO credit;
        try {
            credit = conveyorClient.calculation(scoringData);
        } catch (ConveyorException e) {
            repositoryService.updateApplicationStatus(applicationId, ApplicationStatus.CC_DENIED);
            notificationProducer.produceApplicationDenied(
                    new EmailMessage(
                            repositoryService.getEmailAddressByApplicationId(applicationId),
                            EmailMessageStatus.APPLICATION_DENIED,
                            applicationId
                    )
            );
            throw e;
        }
        repositoryService.calculate(finishRegistrationRequest, applicationId, credit);

        notificationProducer.produceCreateDocuments(
                new EmailMessage(
                        repositoryService.getEmailAddressByApplicationId(applicationId),
                        EmailMessageStatus.CREATE_DOCUMENTS,
                        applicationId
                )
        );
    }
}

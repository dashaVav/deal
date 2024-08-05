package com.example.deal.service.impl;

import com.example.deal.dto.*;
import com.example.deal.dto.enums.EmailMessageStatus;
import com.example.deal.exception.ConveyorException;
import com.example.deal.exception.UnresolvedOperationException;
import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.service.ConveyorClient;
import com.example.deal.service.DealService;
import com.example.deal.service.NotificationProducer;
import com.example.deal.service.RepositoryService;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final RepositoryService repositoryService;
    private final ConveyorClient conveyorClient;
    private final NotificationProducer notificationProducer;
    private final Counter approvedApplicationCounter;
    private static final String UNRESOLVED_OPERATION_MESSAGE = "The operation is performed in the wrong sequence";

    @Override
    @Transactional
    public List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest) {
        Long applicationId = repositoryService.createApplicationWithClient(loanApplicationRequest);
        List<LoanOfferDTO> loanOffers = setApplicationId(applicationId, conveyorClient.offers(loanApplicationRequest));
        repositoryService.saveLoanOffers(applicationId, loanOffers);
        return loanOffers;
    }

    private List<LoanOfferDTO> setApplicationId(long applicationId, List<LoanOfferDTO> loanOfferDTOS) {
        loanOfferDTOS.forEach(loanOffer -> loanOffer.setApplicationId(applicationId));
        return loanOfferDTOS;
    }

    @Override
    public void offer(LoanOfferDTO loanOffer) {
        ApplicationStatus applicationStatus = repositoryService.getApplicationStatus(loanOffer.getApplicationId());
        if (!(
                applicationStatus.equals(ApplicationStatus.PREAPPROVAL)
                        || applicationStatus.equals(ApplicationStatus.APPROVED)
        )) {
            throw new UnresolvedOperationException(UNRESOLVED_OPERATION_MESSAGE);
        }
        repositoryService.validateOffer(loanOffer);
        repositoryService.offer(loanOffer);
        approvedApplicationCounter.increment();

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
        ApplicationStatus applicationStatus = repositoryService.getApplicationStatus(applicationId);
        if (!(
                applicationStatus.equals(ApplicationStatus.APPROVED)
                        || applicationStatus.equals(ApplicationStatus.CC_APPROVED)
        )) {
            throw new UnresolvedOperationException(UNRESOLVED_OPERATION_MESSAGE);
        }

        repositoryService.saveClientAdditionalInfo(finishRegistrationRequest, applicationId);
        ScoringDataDTO scoringData = repositoryService.getScoringData(finishRegistrationRequest, applicationId);

        CreditDTO credit;
        try {
            credit = conveyorClient.calculation(scoringData);
        } catch (ConveyorException e) {
            if (e.getStatus() == 403) {
                repositoryService.setApplicationStatus(applicationId, ApplicationStatus.CC_DENIED);
                notificationProducer.produceApplicationDenied(
                        new EmailMessage(
                                repositoryService.getEmailAddressByApplicationId(applicationId),
                                EmailMessageStatus.APPLICATION_DENIED,
                                applicationId
                        )
                );
            }
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

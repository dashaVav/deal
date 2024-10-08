package com.example.deal.service.impl;

import com.example.deal.dto.EmailMessage;
import com.example.deal.dto.SesCodeDTO;
import com.example.deal.dto.enums.EmailMessageStatus;
import com.example.deal.exception.InvalidSesCodeException;
import com.example.deal.exception.UnresolvedOperationException;
import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.model.enums.CreditStatus;
import com.example.deal.service.DocumentService;
import com.example.deal.service.NotificationProducer;
import com.example.deal.service.RepositoryService;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final RepositoryService repositoryService;
    private final NotificationProducer notificationProducer;
    private final Counter creditIssuedCounter;

    private static final String UNRESOLVED_OPERATION = "The operation is performed in the wrong sequence";

    @Override
    public void sendDocuments(Long applicationId) {
        ApplicationStatus applicationStatus = repositoryService.getApplicationStatus(applicationId);
        if (!(
                applicationStatus.equals(ApplicationStatus.CC_APPROVED)
                        || applicationStatus.equals(ApplicationStatus.PREPARE_DOCUMENTS)
                        || applicationStatus.equals(ApplicationStatus.DOCUMENT_CREATED)
        )) {
            throw new UnresolvedOperationException(UNRESOLVED_OPERATION);
        }
        repositoryService.setApplicationStatus(applicationId, ApplicationStatus.PREPARE_DOCUMENTS);
        notificationProducer.produceSendDocuments(
                new EmailMessage(
                        repositoryService.getEmailAddressByApplicationId(applicationId),
                        EmailMessageStatus.SEND_DOCUMENTS,
                        applicationId
                )
        );
    }

    @Override
    public void signDocuments(Long applicationId) {
        if (!repositoryService.getApplicationStatus(applicationId).equals(ApplicationStatus.DOCUMENT_CREATED)) {
            throw new UnresolvedOperationException(UNRESOLVED_OPERATION);
        }
        repositoryService.setSesCode(applicationId, String.valueOf(UUID.randomUUID()));
        notificationProducer.produceSendSes(
                new EmailMessage(
                        repositoryService.getEmailAddressByApplicationId(applicationId),
                        EmailMessageStatus.SEND_SES,
                        applicationId
                )
        );
    }

    @Override
    public void verifyDocuments(Long applicationId, SesCodeDTO sesCode) {
        if (!repositoryService.getApplicationStatus(applicationId).equals(ApplicationStatus.DOCUMENT_CREATED)) {
            throw new UnresolvedOperationException(UNRESOLVED_OPERATION);
        }

        if (!sesCode.getCode().equals(repositoryService.getSesCode(applicationId))) {
            repositoryService.setApplicationStatus(applicationId, ApplicationStatus.CLIENT_DENIED);
            notificationProducer.produceApplicationDenied(
                    new EmailMessage(
                            repositoryService.getEmailAddressByApplicationId(applicationId),
                            EmailMessageStatus.APPLICATION_DENIED,
                            applicationId
                    )
            );
            throw new InvalidSesCodeException("Invalid ses code.");
        }

        repositoryService.setApplicationStatus(applicationId, ApplicationStatus.DOCUMENT_SIGNED);
        repositoryService.setSignDate(applicationId);
        repositoryService.setApplicationStatus(applicationId, ApplicationStatus.CREDIT_ISSUED);
        repositoryService.setCreditStatus(applicationId, CreditStatus.ISSUED);
        creditIssuedCounter.increment();
        notificationProducer.produceCreditIssued(
                new EmailMessage(
                        repositoryService.getEmailAddressByApplicationId(applicationId),
                        EmailMessageStatus.CREDIT_ISSUED,
                        applicationId
                )
        );
    }
}

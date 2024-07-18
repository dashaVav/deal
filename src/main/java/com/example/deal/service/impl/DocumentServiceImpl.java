package com.example.deal.service.impl;

import com.example.deal.dto.EmailMessage;
import com.example.deal.dto.SesCodeDTO;
import com.example.deal.dto.enums.EmailMessageStatus;
import com.example.deal.exception.InvalidSesCodeException;
import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.service.DocumentService;
import com.example.deal.service.RepositoryService;
import com.example.deal.service.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final RepositoryService repositoryService;
    private final NotificationProducer notificationProducer;

    @Override
    public void sendDocuments(Long applicationId) {
        repositoryService.updateApplicationStatus(applicationId, ApplicationStatus.PREPARE_DOCUMENTS);
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
        if (!sesCode.getCode().equals(repositoryService.getSesCode(applicationId))) {
            repositoryService.updateApplicationStatus(applicationId, ApplicationStatus.CLIENT_DENIED);
            notificationProducer.produceApplicationDenied(
                    new EmailMessage(
                            repositoryService.getEmailAddressByApplicationId(applicationId),
                            EmailMessageStatus.APPLICATION_DENIED,
                            applicationId
                    )
            );
            throw new InvalidSesCodeException("Invalid ses code.");
        }

        repositoryService.updateApplicationStatus(applicationId, ApplicationStatus.DOCUMENT_SIGNED);
        repositoryService.setCreationDate(applicationId);
        repositoryService.updateApplicationStatus(applicationId, ApplicationStatus.CREDIT_ISSUED);
        notificationProducer.produceCreditIssued(
                new EmailMessage(
                        repositoryService.getEmailAddressByApplicationId(applicationId),
                        EmailMessageStatus.CREDIT_ISSUED,
                        applicationId
                )
        );
    }
}

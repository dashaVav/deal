package com.example.deal.service;

import com.example.deal.dto.AuditActionDTO;
import com.example.deal.dto.EmailMessage;

public interface NotificationProducer {
    void produceFinishRegistration(EmailMessage emailMessage);

    void produceCreateDocuments(EmailMessage emailMessage);

    void produceSendDocuments(EmailMessage emailMessage);

    void produceSendSes(EmailMessage emailMessage);

    void produceCreditIssued(EmailMessage emailMessage);

    void produceApplicationDenied(EmailMessage emailMessage);

    void produceAuditAction(AuditActionDTO auditAction);
}

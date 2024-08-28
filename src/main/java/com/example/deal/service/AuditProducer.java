package com.example.deal.service;

import com.example.deal.dto.AuditActionDTO;

public interface AuditProducer {
    void produceAuditAction(AuditActionDTO auditAction);
}

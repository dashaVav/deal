package com.example.deal.service.kafka;

import com.example.deal.dto.AuditActionDTO;
import com.example.deal.service.AuditProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditProducerImpl implements AuditProducer {
    private final KafkaTemplate<String, AuditActionDTO> auditActionKafkaTemplate;
    @Value("${spring.kafka.topic.audit}")
    private String auditActionTopic;

    @Override
    public void produceAuditAction(AuditActionDTO auditAction) {
        auditActionKafkaTemplate.send(auditActionTopic, auditAction);
    }
}

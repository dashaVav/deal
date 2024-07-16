package com.example.deal.service.kafka;

import com.example.deal.dtos.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, EmailMessage> kafkaProducer;

    @Value("${spring.kafka.topic.finish-registration}")
    private String finishRegistrationTopic;

    public void produceFinishRegistration(EmailMessage emailMessage) {
        kafkaProducer.send(finishRegistrationTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.create-documents}")
    private String createDocumentsTopic;

    public void produceCreateDocuments(EmailMessage emailMessage) {
        kafkaProducer.send(createDocumentsTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.send-documents}")
    private String sendDocumentsTopic;

    public void produceSendDocuments(EmailMessage emailMessage) {
        kafkaProducer.send(sendDocumentsTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.send-ses}")
    private String sendSesTopic;

    public void produceSendSes(EmailMessage emailMessage) {
        kafkaProducer.send(sendSesTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.credit-issued}")
    private String creditIssuedTopic;

    public void produceCreditIssued(EmailMessage emailMessage) {
        kafkaProducer.send(creditIssuedTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.application-denied}")
    private String applicationDeniedTopic;

    public void produceApplicationDenied(EmailMessage emailMessage) {
        kafkaProducer.send(applicationDeniedTopic, emailMessage);
    }
}

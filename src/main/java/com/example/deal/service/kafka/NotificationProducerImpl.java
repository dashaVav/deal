package com.example.deal.service.kafka;

import com.example.deal.dto.EmailMessage;
import com.example.deal.service.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducerImpl implements NotificationProducer {
    private final KafkaTemplate<String, EmailMessage> kafkaProducer;

    @Value("${spring.kafka.topic.finish-registration}")
    private String finishRegistrationTopic;

    @Override
    public void produceFinishRegistration(EmailMessage emailMessage) {
        kafkaProducer.send(finishRegistrationTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.create-documents}")
    private String createDocumentsTopic;

    @Override
    public void produceCreateDocuments(EmailMessage emailMessage) {
        kafkaProducer.send(createDocumentsTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.send-documents}")
    private String sendDocumentsTopic;

    @Override
    public void produceSendDocuments(EmailMessage emailMessage) {
        kafkaProducer.send(sendDocumentsTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.send-ses}")
    private String sendSesTopic;

    @Override
    public void produceSendSes(EmailMessage emailMessage) {
        kafkaProducer.send(sendSesTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.credit-issued}")
    private String creditIssuedTopic;

    @Override
    public void produceCreditIssued(EmailMessage emailMessage) {
        kafkaProducer.send(creditIssuedTopic, emailMessage);
    }


    @Value("${spring.kafka.topic.application-denied}")
    private String applicationDeniedTopic;

    @Override
    public void produceApplicationDenied(EmailMessage emailMessage) {
        kafkaProducer.send(applicationDeniedTopic, emailMessage);
    }
}

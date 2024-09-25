package com.example.deal.service.kafka;

import com.example.deal.dto.EmailMessage;
import com.example.deal.service.NotificationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProducerImpl implements NotificationProducer {
    private final KafkaTemplate<String, EmailMessage> emailMessageKafkaTemplate;

    @Value("${spring.kafka.topic.finish-registration}")
    private String finishRegistrationTopic;

    private void printLog(String topic, Long applicationId) {
        log.info("Message with application id - {} sent to topic - {}",
                applicationId, topic);
    }

    @Override
    public void produceFinishRegistration(EmailMessage emailMessage) {
        emailMessageKafkaTemplate.send(finishRegistrationTopic, emailMessage);
        printLog(finishRegistrationTopic, emailMessage.getApplicationId());
    }


    @Value("${spring.kafka.topic.create-documents}")
    private String createDocumentsTopic;

    @Override
    public void produceCreateDocuments(EmailMessage emailMessage) {
        emailMessageKafkaTemplate.send(createDocumentsTopic, emailMessage);
        printLog(createDocumentsTopic, emailMessage.getApplicationId());
    }


    @Value("${spring.kafka.topic.send-documents}")
    private String sendDocumentsTopic;

    @Override
    public void produceSendDocuments(EmailMessage emailMessage) {
        emailMessageKafkaTemplate.send(sendDocumentsTopic, emailMessage);
        printLog(sendDocumentsTopic, emailMessage.getApplicationId());
    }


    @Value("${spring.kafka.topic.send-ses}")
    private String sendSesTopic;

    @Override
    public void produceSendSes(EmailMessage emailMessage) {
        emailMessageKafkaTemplate.send(sendSesTopic, emailMessage);
        printLog(sendSesTopic, emailMessage.getApplicationId());
    }


    @Value("${spring.kafka.topic.credit-issued}")
    private String creditIssuedTopic;

    @Override
    public void produceCreditIssued(EmailMessage emailMessage) {
        emailMessageKafkaTemplate.send(creditIssuedTopic, emailMessage);
        printLog(creditIssuedTopic, emailMessage.getApplicationId());
    }


    @Value("${spring.kafka.topic.application-denied}")
    private String applicationDeniedTopic;

    @Override
    public void produceApplicationDenied(EmailMessage emailMessage) {
        emailMessageKafkaTemplate.send(applicationDeniedTopic, emailMessage);
        printLog(applicationDeniedTopic, emailMessage.getApplicationId());
    }
}

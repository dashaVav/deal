package com.example.deal.annotation;

import com.example.deal.dto.AuditActionDTO;
import com.example.deal.dto.enums.Service;
import com.example.deal.dto.enums.Type;
import com.example.deal.service.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditHandler {
    private final NotificationProducer notificationProducer;

    @Around("@annotation(auditAction)")
    public Object logAround(ProceedingJoinPoint joinPoint, AuditAction auditAction) throws Throwable {
        Long applicationId = getApplicationId(joinPoint.getArgs());
        String message = auditAction.message();

        AuditActionDTO startAuditActionDTO = new AuditActionDTO();
        startAuditActionDTO.setId(UUID.randomUUID())
                .setType(Type.START)
                .setService(Service.DEAL)
                .setMessage(LocalDateTime.now() + " - " + message + " - application id -" + applicationId);
        notificationProducer.produceAuditAction(startAuditActionDTO);

        try {
            Object result = joinPoint.proceed();

            AuditActionDTO auditActionDTO = new AuditActionDTO();
            auditActionDTO.setId(UUID.randomUUID())
                    .setType(Type.SUCCESS)
                    .setService(Service.DEAL)
                    .setMessage(LocalDateTime.now() + " - " + message + " - application id -" + applicationId);
            notificationProducer.produceAuditAction(auditActionDTO);
            return result;
        } catch (Exception e) {
            AuditActionDTO auditActionDTO = new AuditActionDTO();
            auditActionDTO.setId(UUID.randomUUID())
                    .setType(Type.FAILURE)
                    .setService(Service.DEAL)
                    .setMessage(LocalDateTime.now() + " - " + message + " - application id -" + applicationId);
            notificationProducer.produceAuditAction(auditActionDTO);
            throw e;
        }
    }

    private Long getApplicationId(Object[] args) {
        String argsToString = Arrays.toString(args);

        Pattern pattern = Pattern.compile("applicationId=(\\d+)");//|[(\d+)]
        Matcher matcher = pattern.matcher(argsToString);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        } else {
            return (long) -1;
        }
    }
}

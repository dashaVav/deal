package com.example.deal.annotation;

import com.example.deal.dto.AuditActionDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.dto.enums.ServiceDTO;
import com.example.deal.dto.enums.Type;
import com.example.deal.service.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditHandler {
    private final NotificationProducer notificationProducer;

    @Around("@annotation(auditAction)")
    public Object logAround(ProceedingJoinPoint joinPoint, AuditAction auditAction) throws Throwable {
        Long applicationId = getApplicationId(joinPoint.getArgs());
        String format = "%s - %s Application id - %d.";

        notificationProducer.produceAuditAction(new AuditActionDTO(
                UUID.randomUUID(), Type.START, ServiceDTO.DEAL,
                String.format(format, LocalDateTime.now(), auditAction.message(), applicationId)
        ));

        try {
            Object result = joinPoint.proceed();

            notificationProducer.produceAuditAction(new AuditActionDTO(
                    UUID.randomUUID(), Type.SUCCESS, ServiceDTO.DEAL,
                    String.format(format, LocalDateTime.now(), auditAction.message(), applicationId)
            ));

            return result;
        } catch (Exception e) {
            notificationProducer.produceAuditAction(new AuditActionDTO(
                    UUID.randomUUID(), Type.FAILURE, ServiceDTO.DEAL,
                    String.format(format, LocalDateTime.now(), auditAction.message(), applicationId)
                            + " Exception message: " + e.getMessage()
            ));
            throw e;
        }
    }

    private Long getApplicationId(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Long number) {
                return number;
            }
            if (args[0] instanceof LoanOfferDTO loanOfferDTO) {
                return loanOfferDTO.getApplicationId();
            }
        }
        return null;
    }
}

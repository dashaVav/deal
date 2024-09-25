package com.example.deal.controller.impl;

import com.example.deal.annotation.AuditAction;
import com.example.deal.controller.AdminController;
import com.example.deal.dto.SesCodeDTO;
import com.example.deal.model.Application;
import com.example.deal.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {
    private final AdminService adminService;

    @Override
    public ResponseEntity<SesCodeDTO> getSesCode(Long applicationId) {
        log.info("/deal/admin/application/{applicationId}/code requested with id - {}", applicationId);
        return ResponseEntity.ok(adminService.getSesCode(applicationId));
    }

    @AuditAction(message = "Application by id requested.")
    @Override
    public ResponseEntity<Application> getApplication(Long applicationId) {
        log.info("/deal/admin/application/{applicationId} requested with id - {}", applicationId);
        return ResponseEntity.ok(adminService.getApplication(applicationId));
    }

    @AuditAction(message = "Get all applications.")
    @Override
    public ResponseEntity<List<Application>> getAllApplications() {
        log.info("/deal/admin/application requested");
        return ResponseEntity.ok(adminService.getAllApplications());
    }

    @Override
    public ResponseEntity<Void> updateApplicationStatus(Long applicationId) {
        log.info("/deal/admin/application/{applicationId}/status requested with id - {}", applicationId);
        adminService.updateApplicationStatusDocCreated(applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

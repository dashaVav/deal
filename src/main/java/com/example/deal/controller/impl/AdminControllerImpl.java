package com.example.deal.controller.impl;

import com.example.deal.controller.AdminController;
import com.example.deal.dto.SesCodeDTO;
import com.example.deal.model.Application;
import com.example.deal.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {
    private final AdminService adminService;

    @Override
    public ResponseEntity<SesCodeDTO> getSesCode(Long applicationId) {
        return ResponseEntity.ok(adminService.getSesCode(applicationId));
    }

    @Override
    public ResponseEntity<Application> getApplication(Long applicationId) {
        return ResponseEntity.ok(adminService.getApplication(applicationId));
    }

    @Override
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(adminService.getAllApplications());
    }

    @Override
    public ResponseEntity<Void> updateApplicationStatus(Long applicationId) {
        adminService.updateApplicationStatusDocCreated(applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

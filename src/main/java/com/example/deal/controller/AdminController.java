package com.example.deal.controller;

import com.example.deal.dto.SesCodeDTO;
import com.example.deal.model.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/deal/admin")
public interface AdminController {
    @GetMapping(path = "/application/{applicationId}")
    ResponseEntity<Application> getApplication(@PathVariable Long applicationId);

    @GetMapping(path = "/application")
    ResponseEntity<List<Application>> getAllApplications();

    @PutMapping(path = "/application/{applicationId}/status")
    ResponseEntity<Void> updateApplicationStatus(@PathVariable Long applicationId);

    @GetMapping(path = "/application/{applicationId}/code")
    ResponseEntity<SesCodeDTO> getSesCode(@PathVariable Long applicationId);
}

package com.example.deal.service;

import com.example.deal.dto.SesCodeDTO;
import com.example.deal.model.Application;

import java.util.List;

public interface AdminService {
    void updateApplicationStatusDocCreated(Long applicationId);

    Application getApplication(Long applicationId);

    List<Application> getAllApplications();

    SesCodeDTO getSesCode(Long applicationId);
}

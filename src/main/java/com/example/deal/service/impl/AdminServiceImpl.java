package com.example.deal.service.impl;

import com.example.deal.dto.SesCodeDTO;
import com.example.deal.model.Application;
import com.example.deal.model.enums.ApplicationStatus;
import com.example.deal.service.AdminService;
import com.example.deal.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final RepositoryService repositoryService;

    @Override
    public void updateApplicationStatusDocCreated(Long applicationId) {
        repositoryService.setApplicationStatus(applicationId, ApplicationStatus.DOCUMENT_CREATED);
    }

    @Override
    public Application getApplication(Long applicationId) {
        return repositoryService.getApplicationById(applicationId);
    }

    @Override
    public List<Application> getAllApplications() {
        return repositoryService.getAllApplications();
    }

    @Override
    public SesCodeDTO getSesCode(Long applicationId) {
        return new SesCodeDTO(repositoryService.getSesCode(applicationId));
    }
}

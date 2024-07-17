package com.example.deal.service;

import com.example.deal.dtos.SesCodeDTO;

public interface DocumentService {
    void sendDocuments(Long applicationId);

    void signDocuments(Long applicationId);

    void verifyDocuments(Long applicationId, SesCodeDTO sesCode);
}

package com.example.deal.controller.impl;

import com.example.deal.controller.DocumentController;
import com.example.deal.dtos.SesCodeDTO;
import com.example.deal.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DocumentControllerImpl implements DocumentController {
    private final DocumentService documentService;

    @Override
    public ResponseEntity<Void> send(Long applicationId) {
        documentService.sendDocuments(applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> sign(Long applicationId) {
        documentService.signDocuments(applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> code(Long applicationId, SesCodeDTO sesCode) {
        documentService.verifyDocuments(applicationId, sesCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

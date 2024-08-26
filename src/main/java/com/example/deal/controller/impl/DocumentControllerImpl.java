package com.example.deal.controller.impl;

import com.example.deal.controller.DocumentController;
import com.example.deal.dto.SesCodeDTO;
import com.example.deal.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DocumentControllerImpl implements DocumentController {
    private final DocumentService documentService;

    @Override
    public ResponseEntity<Void> send(Long applicationId) {
        log.info("/deal/document/{applicationId}/send requested with id - {}", applicationId);
        documentService.sendDocuments(applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> sign(Long applicationId) {
        log.info("/deal/document/{applicationId}/sign requested with id - {}", applicationId);
        documentService.signDocuments(applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> code(Long applicationId, SesCodeDTO sesCode) {
        log.info("/deal/document/{applicationId}/code requested with id - {}", applicationId);
        documentService.verifyDocuments(applicationId, sesCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

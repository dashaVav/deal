package com.example.deal.controller;

import com.example.deal.dtos.SesCodeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/deal/document")
public interface DocumentController {
    @PostMapping(path = "/{applicationId}/send")
    ResponseEntity<Void> send(@PathVariable Long applicationId);

    @PostMapping(path = "/{applicationId}/sign")
    ResponseEntity<Void> sign(@PathVariable Long applicationId);

    @PostMapping(path = "/{applicationId}/code")
    ResponseEntity<Void> code(@PathVariable Long applicationId, @RequestBody SesCodeDTO sesCode);

}

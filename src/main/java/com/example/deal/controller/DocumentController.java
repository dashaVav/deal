package com.example.deal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/deal/document/{applicationId}/")
public interface DocumentController {
    @PostMapping(path = "/send")
    ResponseEntity<Void> send(@PathVariable Long applicationId);

    @PostMapping(path = "/sign")
    ResponseEntity<Void> sign(@PathVariable Long applicationId);

    @PostMapping(path = "/code")
    ResponseEntity<Void> code(@PathVariable Long applicationId);

}

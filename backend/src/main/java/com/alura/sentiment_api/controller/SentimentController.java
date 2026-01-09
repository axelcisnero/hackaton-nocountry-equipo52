package com.alura.sentiment_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.sentiment_api.dto.SentimentRequest;
import com.alura.sentiment_api.dto.SentimentResponse;
import com.alura.sentiment_api.service.SentimentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SentimentController {

    private final SentimentService sentimentService;

    public SentimentController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @PostMapping("/sentiment")
    public ResponseEntity<SentimentResponse> getSentiment(@Valid @RequestBody SentimentRequest Request) {
        SentimentResponse response = sentimentService.getSentiment(Request);
        return ResponseEntity.ok(response);
    }

}

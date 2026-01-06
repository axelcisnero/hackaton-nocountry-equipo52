package com.alura.sentiment_api.service;

import com.alura.sentiment_api.dto.SentimentRequest;
import com.alura.sentiment_api.dto.SentimentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SentimentService {

    private final String API_URL = "http://localhost:5000/predict";
    private final RestTemplate restTemplate;

    public SentimentService() {
        this.restTemplate = new RestTemplate();
    }
    public SentimentResponse obtenerSentimiento(SentimentRequest request) {
        return restTemplate.postForObject(API_URL, request, SentimentResponse.class);
    }

}

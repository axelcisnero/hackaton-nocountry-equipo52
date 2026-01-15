package com.alura.sentiment_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alura.sentiment_api.dto.SentimentRequest;
import com.alura.sentiment_api.dto.SentimentResponse;
import com.alura.sentiment_api.exception.DataScienceServiceException;
import com.alura.sentiment_api.model.entity.SentimentLog;
import com.alura.sentiment_api.repository.SentimentRepository;
import com.alura.sentiment_api.exception.DataScienceServiceException;

@Service
public class SentimentService {

    @Value("${ds.api.url:http://localhost:5000/predict}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final SentimentRepository sentimentRepository;

    public SentimentService(RestTemplate restTemplate, SentimentRepository sentimentRepository) {
        this.restTemplate = restTemplate;
        this.sentimentRepository = sentimentRepository;
    }

    public SentimentResponse getSentiment(SentimentRequest request) {
        try {
            SentimentResponse response = restTemplate.postForObject(apiUrl, request, SentimentResponse.class);

            if (response != null) {
                saveAnalysis(request.getText(), response);
            }

            return response;

        } catch (Exception e) {
            throw new DataScienceServiceException(
                    "Error de conexión con el servicio de DataScience: " + e.getMessage());
        }
    }

    private void saveAnalysis(String text, SentimentResponse response) {
        SentimentLog log = new SentimentLog();
        log.setText(text);
        log.setPrediction(response.getPrediction());
        log.setProbability(response.getProbability());
        sentimentRepository.save(log);
    }
}
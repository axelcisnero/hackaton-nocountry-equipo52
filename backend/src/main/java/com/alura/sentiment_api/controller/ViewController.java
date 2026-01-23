package com.alura.sentiment_api.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alura.sentiment_api.dto.SentimentRequest;
import com.alura.sentiment_api.dto.SentimentResponse;
import com.alura.sentiment_api.repository.SentimentRepository;
import com.alura.sentiment_api.service.SentimentService;

@Controller
public class ViewController {

    private final SentimentService sentimentService;
    private final SentimentRepository sentimentRepository;

    public ViewController(SentimentService sentimentService, SentimentRepository sentimentRepository) {
        this.sentimentService = sentimentService;
        this.sentimentRepository = sentimentRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        var history = sentimentRepository.findAll();
        model.addAttribute("history", history != null ? history : new ArrayList<>());
        return "index";
    }

    @PostMapping("/analyze")
    public String analyze(@RequestParam String text, Model model) {
        SentimentRequest request = new SentimentRequest();
        request.setText(text);
        SentimentResponse response = sentimentService.getSentiment(request);
        model.addAttribute("response", response);
        model.addAttribute("history", sentimentRepository.findAll());

        return "index";
    }

    @GetMapping("/dashboard")
    public String verDashboard(Model model) {
        model.addAttribute("stats", sentimentService.getStats());
        return "stats";
    }
}
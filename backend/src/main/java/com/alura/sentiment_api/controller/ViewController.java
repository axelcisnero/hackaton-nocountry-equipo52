package com.alura.sentiment_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam; // Importante para recibir el texto

import com.alura.sentiment_api.dto.SentimentRequest;
import com.alura.sentiment_api.dto.SentimentResponse;
import com.alura.sentiment_api.service.SentimentService;
import com.alura.sentiment_api.repository.SentimentRepository; // Importa tu repositorio

import java.util.ArrayList;

@Controller
public class ViewController {

    private final SentimentService sentimentService;
    private final SentimentRepository sentimentRepository; // Agregamos el repositorio

    // Actualizamos el constructor para inyectar ambos
    public ViewController(SentimentService sentimentService, SentimentRepository sentimentRepository) {
        this.sentimentService = sentimentService;
        this.sentimentRepository = sentimentRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Obtenemos el historial de la base de datos
        var history = sentimentRepository.findAll();
        // Pasamos la lista al modelo (si es null, enviamos lista vacía)
        model.addAttribute("history", history != null ? history : new ArrayList<>());
        return "index";
    }

    @PostMapping("/analyze")
    public String analyze(@RequestParam String text, Model model) {
        SentimentRequest request = new SentimentRequest();
        request.setText(text);

        // 1. Obtener la respuesta del servicio (que también guarda en DB)
        SentimentResponse response = sentimentService.getSentiment(request);
        model.addAttribute("response", response);

        // 2. IMPORTANTE: Volver a cargar el historial actualizado para la tabla y gráfica
        model.addAttribute("history", sentimentRepository.findAll());
        
        return "index";
    }
}
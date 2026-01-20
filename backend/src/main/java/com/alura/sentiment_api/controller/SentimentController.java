package com.alura.sentiment_api.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alura.sentiment_api.dto.SentimentRequest;
import com.alura.sentiment_api.dto.SentimentResponse;
import com.alura.sentiment_api.dto.StatsResponse;
import com.alura.sentiment_api.repository.SentimentRepository;
import com.alura.sentiment_api.service.SentimentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Controlador REST para operaciones de análisis de sentimientos.
 * Proporciona endpoints para analizar el sentimiento de texto y obtener estadísticas de sentimiento.
 */
@RestController
@RequestMapping("/api/v1")
@SuppressWarnings("unused")
public class SentimentController {

    private final SentimentService sentimentService;
    private final SentimentRepository sentimentRepository;

    /**
     * Construye SentimentController con las dependencias requeridas.
     *
     * @param sentimentService servicio para manejar solicitudes de análisis de sentimiento
     * @param sentimentRepository repositorio para acceder a los datos de logs de sentimiento
     */
    public SentimentController(SentimentService sentimentService, SentimentRepository sentimentRepository) {
        this.sentimentService = sentimentService;
        this.sentimentRepository = sentimentRepository;
    }

    /**
     * Analiza el sentimiento del texto proporcionado.
     *
     * @param Request cuerpo de la solicitud que contiene el texto a analizar
     * @return resultado del análisis de sentimiento con predicción y probabilidad
     */
    @PostMapping("/sentiment")
    public ResponseEntity<SentimentResponse> getSentiment(@Valid @RequestBody SentimentRequest Request) {
        SentimentResponse response = sentimentService.getSentiment(Request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene estadísticas de sentimiento de los logs más recientes.
     *
     * @param last cantidad de logs más recientes a considerar (por defecto 100, mínimo 1, máximo 1000)
     * @return estadísticas que incluyen total y porcentajes para positivo/negativo
     */
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> stats(
            @RequestParam(defaultValue = "100") @Min(1) @Max(1000) int last) {
        var page = PageRequest.of(0, last, Sort.by(Sort.Direction.DESC, "createdAt"));
        var logs = sentimentRepository.findAll(page).getContent();

        long total = logs.size();
        long positivo = logs.stream()
                .filter(l -> "positivo".equalsIgnoreCase(l.getPrediction()))
                .count();
        long negativo = logs.stream()
                .filter(l -> "negativo".equalsIgnoreCase(l.getPrediction()))
                .count();

        double positivoPct = total == 0 ? 0.0 : (positivo * 100.0) / total;
        double negativoPct = total == 0 ? 0.0 : (negativo * 100.0) / total;

        StatsResponse response = StatsResponse.builder()
                .last(last)
                .total(total)
                .positivoPct(positivoPct)
                .negativoPct(negativoPct)
                .build();

        return ResponseEntity.ok(response);
    }

}

package com.alura.sentiment_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de salud del servicio.
 *
 * ¿Por qué existe?
 * - Permite verificar rápidamente si la API está viva.
 * - No depende de base de datos ni de Data Science.
 * - Se usa para pruebas, monitoreo y despliegues.
 */
@RestController
public class HealthController {

    /**
     * Endpoint mínimo de verificación.
     * GET /health
     *
     * Respuesta esperada:
     * 200 OK
     * "OK"
     */
    @GetMapping("/api/v1/health")
    public String health() {
        return "OK";
    }
}
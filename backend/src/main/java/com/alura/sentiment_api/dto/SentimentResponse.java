package com.alura.sentiment_api.dto;

import lombok.Data;

/**
 * DTO de salida (Response).
 * Representa el JSON que la API devuelve al cliente.
 *
 * Ejemplo de JSON:
 * {
 * "prediction": "Positivo",
 * "probability": 0.87
 * }
 *
 * NOTA: Esto no es base de datos. Es contrato de respuesta.
 */
@Data
public class SentimentResponse {

    private String prediction; // "Positivo", "Negativo", "Neutro"
    private Double probability; // 0.0 a 1.0 (confianza del modelo)

}

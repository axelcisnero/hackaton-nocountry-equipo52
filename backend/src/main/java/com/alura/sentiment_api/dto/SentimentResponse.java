package com.alura.sentiment_api.dto;

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
public class SentimentResponse {

    private String prediction; // "Positivo", "Negativo", "Neutro"
    private Double probability; // 0.0 a 1.0 (confianza del modelo)

    // Por qué: constructor vacío requerido por algunas herramientas/librerías y
    // para flexibilidad.
    public SentimentResponse() {
    }

    public SentimentResponse(String prediction, Double probability) {
        this.prediction = prediction;
        this.probability = probability;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }
}
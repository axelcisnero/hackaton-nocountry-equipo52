package com.alura.sentiment_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada (Request).
 * Representa exactamente el JSON que recibirá el backend.
 *
 * Ejemplo de JSON esperado:
 * {
 * "text": "Me encantó el servicio"
 * }
 *
 * NOTA: Esto NO es una entidad de base de datos (eso iría en model/entity).
 * Es un contrato de comunicación (API <-> cliente/DS).
 */
public class SentimentRequest {

    // Por qué: evitamos recibir null, vacío o solo espacios.
    @NotBlank(message = "text is required")
    // Por qué: prevenimos textos muy cortos o extremadamente largos.
    @Size(min = 3, max = 2000, message = "text must be between 3 and 2000 characters")
    private String text;

    // Por qué: Spring/Jackson necesita un constructor vacío para deserializar JSON
    // a objeto.
    public SentimentRequest() {
    }

    public SentimentRequest(String text) {
        this.text = text;
    }

    // Por qué: getters/setters permiten que Jackson mapee el JSON correctamente.
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

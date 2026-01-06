package com.alura.sentiment_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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
@Data
public class SentimentRequest {

    // Por qué: evitamos recibir null, vacío o solo espacios.
    @NotBlank(message = "El texto no puede estar vacío")
    // Por qué: prevenimos textos muy cortos o extremadamente largos.
    @Size(min = 3, max = 2000, message = "El texto debe tener entre 3 y 2000 caracteres")
    private String text;

}

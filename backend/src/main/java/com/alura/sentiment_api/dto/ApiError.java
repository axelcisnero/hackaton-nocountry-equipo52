package com.alura.sentiment_api.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 DTO estándar para respuestas de error.
 Objetivo: devolver siempre un JSON consistente y entendible para el cliente.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private Instant timestamp; // Momento del error
    private int status; // 400, 500, 503, etc.
    private String error; // Bad Request, Internal Server Error, etc.
    private String message; // Mensaje general
    private String path; // Endpoint donde ocurrió
    private List<FieldDetail> details; // Lista de errores por campo (si aplica)

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldDetail {
        private String field;
        private String message;
    }
}

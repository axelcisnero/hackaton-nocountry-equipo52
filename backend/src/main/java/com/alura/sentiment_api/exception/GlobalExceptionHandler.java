package com.alura.sentiment_api.exception;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alura.sentiment_api.dto.ApiError;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Caso 1: Validaciones del @Valid (texto vacío, demasiado largo, etc.)
     * Devuelve 400 con detalles por campo.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<ApiError.FieldDetail> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldDetail)
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validación fallida")
                .path(request.getRequestURI())
                .details(details)
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

    /*
     * Caso 2: JSON mal formado o cuerpo inválido (por ejemplo, faltan llaves o
     * comillas)
     * Devuelve 400 sin stacktrace.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMalformedJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("El cuerpo de la solicitud (JSON) es inválido o está mal formado")
                .path(request.getRequestURI())
                .details(null)
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

    /*
     * Caso 3: Error controlado cuando el servicio de Data Science no está
     * disponible.
     * Devuelve 503.
     */
    @ExceptionHandler(DataScienceServiceException.class)
    public ResponseEntity<ApiError> handleDataScienceServiceError(
            DataScienceServiceException ex,
            HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .error(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .details(null)
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(apiError);
    }

    /*
     * Caso 4: Cualquier error no controlado.
     * Devuelve 500 con mensaje genérico (sin stacktrace al cliente).
     * IMPORTANTE: el detalle real se revisa en logs del backend, no en la
     * respuesta.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericError(
            Exception ex,
            HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Ocurrió un error interno. Intenta nuevamente.")
                .path(request.getRequestURI())
                .details(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    private ApiError.FieldDetail toFieldDetail(FieldError fieldError) {
        return ApiError.FieldDetail.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }
}
package com.alura.sentiment_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para el endpoint de estadísticas de sentimiento.
 * Contiene el número de registros considerados, el total y los porcentajes de sentimientos positivos y negativos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {

    /**
     * Cantidad de registros más recientes que se consideraron para el cálculo.
     */
    private int last;

    /**
     * Total de registros analizados.
     */
    private long total;

    /**
     * Porcentaje de sentimientos positivos sobre el total.
     */
    private double positivoPct;

    /**
     * Porcentaje de sentimientos negativos sobre el total.
     */
    private double negativoPct;
}

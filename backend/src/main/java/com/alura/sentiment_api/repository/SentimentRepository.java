package com.alura.sentiment_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alura.sentiment_api.model.entity.SentimentLog;

/**
 * Interface Repository para SentimentLog.
 * Hereda todos los métodos CRUD (Save, Find, Delete) automáticamente.
 */
@Repository
public interface SentimentRepository extends JpaRepository<SentimentLog, Long> {
    // Aquí podrías añadir métodos personalizados para las estadísticas después
}
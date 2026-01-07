# Validación Cruzada K-Fold (k=5) – TF-IDF + Logistic Regression

## Dataset
- Archivo: `dataset_sentimientos_ecom (1).csv`
- Clasificación binaria: positivo vs negativo
- La clase "neutro" fue excluida para este análisis
- Registros utilizados: 750 (400 positivos, 350 negativos)

## Pipeline evaluado
- Limpieza básica de texto (minúsculas, eliminación de URLs y menciones)
- Vectorización: TF-IDF (ngram_range=(1,2), max_features=20000, min_df=2)
- Modelo: Logistic Regression (class_weight='balanced', max_iter=2000)

## Configuración de validación
- Esquema: StratifiedKFold
- Número de folds: 5
- Shuffle: True
- Random state: 42
- Métrica principal: F1-score

## Resultados por fold

| Fold | F1 Train | F1 Test | Accuracy | Precision | Recall | Gap Train-Test |
|-----|----------|---------|----------|-----------|--------|----------------|
| 1 | 1.00 | 1.00 | 1.00 | 1.00 | 1.00 | 0.00 |
| 2 | 1.00 | 1.00 | 1.00 | 1.00 | 1.00 | 0.00 |
| 3 | 1.00 | 1.00 | 1.00 | 1.00 | 1.00 | 0.00 |
| 4 | 1.00 | 1.00 | 1.00 | 1.00 | 1.00 | 0.00 |
| 5 | 1.00 | 1.00 | 1.00 | 1.00 | 1.00 | 0.00 |

## Métricas agregadas
- F1-score promedio: 1.00
- Desviación estándar: 0.00
- Accuracy promedio: 1.00
- Precision promedio: 1.00
- Recall promedio: 1.00
- Gap promedio (train-test): 0.00

## Comparación con test set holdout
- Split 80/20 estratificado
- F1-score en test: 1.00
- Diferencia vs F1 promedio de validación cruzada: 0.00%

## Conclusión sobre la robustez del modelo
El modelo cumple todos los criterios de aceptación definidos:
- Resultados consistentes entre folds
- Alta capacidad de generalización
- No se observa overfitting

⚠️ Nota: métricas perfectas pueden indicar que el dataset es relativamente simple o contiene patrones muy repetitivos. Se recomienda validar el modelo con datos reales del dominio del negocio para confirmar su robustez en producción.

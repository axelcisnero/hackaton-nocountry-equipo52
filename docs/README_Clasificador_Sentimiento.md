
# 🧠 Clasificador de Sentimiento - API Hackathon

Este proyecto implementa un modelo de Machine Learning para análisis de sentimiento aplicado a textos de reseñas o comentarios. La solución permite clasificar automáticamente los textos como **Positivo**, **Negativo** o **Neutro**, y está pensada para integrarse con un backend mediante una API REST.

---

## 📦 Contenido del proyecto

- `Clasificador_Sentimiento_API.ipynb`: notebook con todo el flujo de trabajo.
- `modelo_sentimiento.joblib`: modelo entrenado de regresión logística.
- `tfidf_vectorizer.joblib`: vectorizador TF-IDF entrenado para transformar nuevos textos.

---

## 🚀 Cómo ejecutar el modelo

1. Clona este repositorio o sube los archivos a tu entorno de trabajo.
2. Abre el notebook `Clasificador_Sentimiento_API.ipynb`.
3. Asegúrate de tener instalado:
```bash
pip install pandas scikit-learn matplotlib seaborn joblib
```
4. Ejecuta todas las celdas del notebook para entrenar o volver a cargar el modelo.

---

## 🔗 Ejemplo de uso en API

El modelo está pensado para integrarse vía backend en un endpoint tipo:

### Entrada esperada (JSON):
```json
{
  "text": "El producto llegó tarde y dañado."
}
```

### Salida esperada (JSON):
```json
{
  "prevision": "Negativo",
  "probabilidad": 0.92
}
```

---

## 📊 Métricas del modelo

El modelo fue entrenado con regresión logística sobre TF-IDF y muestra un desempeño perfecto sobre los datos de validación:

- Accuracy: 1.0
- Precision / Recall / F1-score por clase: 1.0

Nota: estos valores pueden variar con otros datos.

---

## ✅ Requisitos

- Python 3.8+
- Bibliotecas: `pandas`, `scikit-learn`, `matplotlib`, `joblib`, `seaborn`

---

## 💡 Casos de uso del modelo

- Clasificar comentarios de usuarios automáticamente.
- Detectar quejas o elogios rápidamente.
- Generar estadísticas de satisfacción en tiempo real.

---

## 🛠️ Siguientes pasos sugeridos

- Integrar este modelo en una API con FastAPI o Flask.
- Agregar endpoint `/sentiment` que consuma el modelo y vectorizador.
- Agregar logs y validación de errores en backend.


from flask import Flask, request, jsonify
import joblib
import numpy as np

# Cargar modelo y vectorizador UNA sola vez
vectorizer = joblib.load("tfidf_vectorizer.joblib")
model = joblib.load("modelo_sentimiento.joblib")

app = Flask(__name__)

@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "text is required"}), 400

    text = data["text"]

    # Vectorizar texto
    X = vectorizer.transform([text])

    # Predicción
    prediction = model.predict(X)[0]

    # Probabilidad (si el modelo la soporta)
    if hasattr(model, "predict_proba"):
        probability = float(np.max(model.predict_proba(X)))
    else:
        probability = None

    return jsonify({
        "prediction": str(prediction),
        "probability": probability
    })

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)